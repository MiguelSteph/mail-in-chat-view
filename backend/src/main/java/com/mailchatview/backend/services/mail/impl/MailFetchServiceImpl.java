package com.mailchatview.backend.services.mail.impl;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.mailchatview.backend.configurations.LocalGoogleCredentials;
import com.mailchatview.backend.dtos.FetchQueryDto;
import com.mailchatview.backend.dtos.FetchResultPage;
import com.mailchatview.backend.dtos.GoogleTokensDto;
import com.mailchatview.backend.dtos.MailDto;
import com.mailchatview.backend.entities.User;
import com.mailchatview.backend.services.UserService;
import com.mailchatview.backend.services.mail.GoogleApiOAuthAdapter;
import com.mailchatview.backend.services.mail.MailFetchService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MailFetchServiceImpl implements MailFetchService {

    private static final long TOKEN_EXPIRATION_DELAY_MILLIS = 5_000L;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String FETCH_QUERY_TEMPLATE =
                    "((from:current_user_email AND (to:with_user_email or cc:with_user_email)) " +
                    "OR (from:with_user_email AND (to:current_user_email OR cc:current_user_email)) " +
                    "OR (cc:current_user_email AND cc:with_user_email)) " +
                    "AND after:date_from AND before:date_to";
    private static final String CURRENT_USER_EMAIL_PLACEHOLDER = "current_user_email";
    private static final String WITH_USER_EMAIL_PLACEHOLDER = "with_user_email";
    private static final String DATE_FROM_PLACEHOLDER = "date_from";
    private static final String DATE_TO_PLACEHOLDER = "date_to";

    private static final String TO_HEADER = "To";
    private static final String DATE_HEADER = "Date";
    private static final String SUBJECT_HEADER = "Subject";
    private static final String CC_HEADER = "Cc";
    private static final String FROM_HEADER = "From";

    @Value("${mail-fetch.max-result}")
    private int maxResult;

    private final LocalGoogleCredentials localGoogleCredentials;
    private final UserService userService;
    private final GoogleApiOAuthAdapter googleApiOAuthAdapter;

    public MailFetchServiceImpl(LocalGoogleCredentials localGoogleCredentials,
                                UserService userService,
                                GoogleApiOAuthAdapter googleApiOAuthAdapter) {
        this.localGoogleCredentials = localGoogleCredentials;
        this.userService = userService;
        this.googleApiOAuthAdapter = googleApiOAuthAdapter;
    }

    @Override
    public FetchResultPage fetchMails(User user, FetchQueryDto fetchQuery) {
        try {
            Gmail gmail = getGmailService(user);

            Gmail.Users.Messages.List listRequest = gmail.users()
                    .messages()
                    .list(user.getUserId())
                    .setMaxResults((long) maxResult)
                    .setQ(getSearchQueryString(user, fetchQuery));

            if (fetchQuery.getPageToken() != null) {
                listRequest = listRequest.setPageToken(fetchQuery.getPageToken());
            }
            ListMessagesResponse listMessagesResponse = listRequest.execute();

            FetchResultPage fetchResultPage = new FetchResultPage();

            if (listMessagesResponse != null) {
                List<Message> messages = new ArrayList<>(batchFetchMessageContent(gmail, user, listMessagesResponse.getMessages()));
                fetchResultPage.setPageToken(listMessagesResponse.getNextPageToken());
                fetchResultPage.setMailDtos(messages.stream()
                        .map(this::messageToMailDtoMapper)
                        .collect(Collectors.toList()));
            }

            return fetchResultPage;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private List<Message> batchFetchMessageContent(Gmail gmail, User user, List<Message> partialMessages) throws Exception {
        final List<Message> fullMessageList = new ArrayList<>();

        final JsonBatchCallback<Message> callback = new JsonBatchCallback<>() {
            @Override
            public void onFailure(GoogleJsonError googleJsonError, HttpHeaders httpHeaders) throws IOException {
                throw new RuntimeException(googleJsonError.toPrettyString());
            }

            @Override
            public void onSuccess(Message message, HttpHeaders httpHeaders) throws IOException {
                fullMessageList.add(message);
            }
        };

        BatchRequest batchRequest = gmail.batch();
        for (Message partialMessage : partialMessages) {
            gmail.users().messages().get(user.getUserId(), partialMessage.getId()).setFormat("full").queue(batchRequest, callback);
        }
        batchRequest.execute();
        return fullMessageList;
    }

    private MailDto messageToMailDtoMapper(Message message) {
        final MailDto mailDto = new MailDto();
        for (MessagePartHeader header : message.getPayload().getHeaders()) {
            final String name = header.getName();
            final String value = header.getValue();
            if (FROM_HEADER.equals(name)) {
                mailDto.setFrom(value);
            } else if (TO_HEADER.equals(name)) {
                mailDto.getTo().add(value);
            } else if (SUBJECT_HEADER.equals(name)) {
                mailDto.setSubject(value);
            } else if (CC_HEADER.equals(name)) {
                mailDto.getCcs().add(value);
            }
        }
        mailDto.setDateTime(LocalDateTime.from(
                ZonedDateTime.ofInstant(
                        Instant.ofEpochMilli(message.getInternalDate()), ZoneId.of("UTC")
                )));
        mailDto.setContent(new String(Base64.decodeBase64(
                message.getPayload().getParts().get(0).getBody().getData()),
                StandardCharsets.UTF_8));

        for (MessagePart messagePart : message.getPayload().getParts()) {
            if (messagePart.getFilename() != null) {
                mailDto.getMailAttachments().add(messagePart.getFilename());
            }
        }
        return mailDto;
    }

    private Gmail getGmailService(User user) throws Exception {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        String accessToken = user.getAccessToken();
        String refreshToken = user.getRefreshToken();

        if (isUserAccessTokenExpired(user)) {
            GoogleTokensDto googleTokensDto = reviewToken(user);
            accessToken = googleTokensDto.getAccessToken();
            refreshToken = googleTokensDto.getRefreshToken();
        }

        return new Gmail.Builder(
                HTTP_TRANSPORT,
                JSON_FACTORY,
                credential(HTTP_TRANSPORT, accessToken, refreshToken))
                .setApplicationName(localGoogleCredentials.getProjectId())
                .build();
    }

    private Credential credential(final NetHttpTransport HTTP_TRANSPORT, String accessToken, String refreshToken) {
        return new Credential.Builder(BearerToken.authorizationHeaderAccessMethod())
                .setTransport(HTTP_TRANSPORT)
                .setJsonFactory(JacksonFactory.getDefaultInstance())
                .setClientAuthentication(new ClientParametersAuthentication(localGoogleCredentials.getId(), localGoogleCredentials.getSecret()))
                .setTokenServerEncodedUrl(localGoogleCredentials.getTokenUri())
                .build()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);
    }

    private boolean isUserAccessTokenExpired(User user) {
        return System.currentTimeMillis() > user.getTokenExpirationTimestamp() + TOKEN_EXPIRATION_DELAY_MILLIS;
    }

    private GoogleTokensDto reviewToken(User user) {
        GoogleTokensDto googleTokensDto = googleApiOAuthAdapter.renewAccessAndRefreshToken(user.getRefreshToken());
        userService.updateUser(user, googleTokensDto);
        return googleTokensDto;
    }

    private String getSearchQueryString(User user, FetchQueryDto fetchQuery) {
        return FETCH_QUERY_TEMPLATE
                .replaceAll(CURRENT_USER_EMAIL_PLACEHOLDER, user.getEmail())
                .replaceAll(WITH_USER_EMAIL_PLACEHOLDER, fetchQuery.getWith())
                .replaceAll(DATE_FROM_PLACEHOLDER, fetchQuery.getFrom().format(DateTimeFormatter.ISO_DATE))
                .replaceAll(DATE_TO_PLACEHOLDER, fetchQuery.getTo().format(DateTimeFormatter.ISO_DATE));
    }
}
