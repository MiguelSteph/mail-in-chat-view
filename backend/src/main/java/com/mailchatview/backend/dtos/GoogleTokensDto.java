package com.mailchatview.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoogleTokensDto {
    private final String accessToken;
    private final String refreshToken;
    private final String idToken;
    private final long accessTokenExpiredTimestamp;
}
