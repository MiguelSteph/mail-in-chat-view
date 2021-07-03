package com.mailchatview.backend.services.mail.impl;

import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.mailchatview.backend.configurations.GoogleCredentials;
import com.mailchatview.backend.dtos.GoogleTokensDto;
import com.mailchatview.backend.dtos.UserDto;
import com.mailchatview.backend.services.mail.GoogleApiOAuthAdapter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GoogleApiOAuthAdapterImpl implements GoogleApiOAuthAdapter {

    private final GoogleCredentials credentials;

    @Override
    public GoogleTokensDto exchangeAuthCodeWithToken(String authCode) {
        try {
            GoogleTokenResponse tokenResponse =
                    new GoogleAuthorizationCodeTokenRequest(
                            new NetHttpTransport(),
                            JacksonFactory.getDefaultInstance(),
                            "https://oauth2.googleapis.com/token",
                            credentials.getId(),
                            credentials.getSecret(),
                            authCode,
                            credentials.getRedirectUris().get(0))
                            .execute();

            return new GoogleTokensDto(
                    tokenResponse.getAccessToken(),
                    tokenResponse.getRefreshToken(),
                    tokenResponse.getIdToken(),
                    System.currentTimeMillis() + tokenResponse.getExpiresInSeconds() * 1000
            );
        } catch (Exception ex) {
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
                    credentials.getId(),
                    credentials.getSecret())
                    .execute();
            return new GoogleTokensDto(
                    tokenResponse.getAccessToken(),
                    tokenResponse.getRefreshToken(),
                    tokenResponse.getIdToken(),
                    System.currentTimeMillis() + tokenResponse.getExpiresInSeconds() * 1000
            );
        } catch (Exception ex) {
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
            throw new RuntimeException(ex);
        }
    }
}
