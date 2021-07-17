package com.mailchatview.backend.services.mail.impl;

import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.mailchatview.backend.dtos.GoogleClientInfo;
import com.mailchatview.backend.dtos.GoogleTokensDto;
import com.mailchatview.backend.dtos.UserDto;
import com.mailchatview.backend.repo.GoogleClientCredentialsRepo;
import com.mailchatview.backend.services.mail.GoogleApiOAuthAdapter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class GoogleApiOAuthAdapterImpl implements GoogleApiOAuthAdapter {

    private final GoogleClientCredentialsRepo credentials;

    @Override
    public GoogleClientInfo getGoogleClientInfo() {
        GoogleClientInfo info = new GoogleClientInfo();
        info.setClientId(credentials.getClientId());
        info.setScope(credentials.getScope());
        return info;
    }

    @Override
    public GoogleTokensDto exchangeAuthCodeWithToken(String authCode) {
        try {
            GoogleTokenResponse tokenResponse =
                    new GoogleAuthorizationCodeTokenRequest(
                            new NetHttpTransport(),
                            JacksonFactory.getDefaultInstance(),
                            credentials.getTokenUri(),
                            credentials.getClientId(),
                            credentials.getClientSecret(),
                            authCode,
                            credentials.getRedirectUri())
                            .execute();

            return new GoogleTokensDto(
                    tokenResponse.getAccessToken(),
                    tokenResponse.getRefreshToken(),
                    tokenResponse.getIdToken(),
                    System.currentTimeMillis() + tokenResponse.getExpiresInSeconds() * 1000
            );
        } catch (Exception ex) {
            log.error("Failed to exchange Authorization code with token", ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public GoogleTokensDto renewAccessAndRefreshToken(String refreshToken) {
        try {
            GoogleTokenResponse tokenResponse = new GoogleRefreshTokenRequest(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    refreshToken,
                    credentials.getClientId(),
                    credentials.getClientSecret())
                    .execute();
            return new GoogleTokensDto(
                    tokenResponse.getAccessToken(),
                    tokenResponse.getRefreshToken(),
                    tokenResponse.getIdToken(),
                    System.currentTimeMillis() + tokenResponse.getExpiresInSeconds() * 1000
            );
        } catch (Exception ex) {
            log.error("Failed to renew access token", ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public UserDto getUserProfile(String idToken) {
        try {
            GoogleIdToken googleIdToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(), idToken);
            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            UserDto userDto = new UserDto();
            userDto.setUserId(payload.getSubject());
            userDto.setEmail(payload.getEmail());
            userDto.setPictureUrl((String) payload.get("picture"));
            userDto.setName((String) payload.get("name"));
            userDto.setFamilyName((String) payload.get("family_name"));
            return userDto;
        } catch (Exception ex) {
            log.error("Failed to parse google token to get user profile", ex);
            throw new RuntimeException(ex);
        }
    }
}
