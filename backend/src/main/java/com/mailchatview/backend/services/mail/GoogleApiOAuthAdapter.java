package com.mailchatview.backend.services.mail;

import com.mailchatview.backend.dtos.GoogleTokensDto;
import com.mailchatview.backend.dtos.UserDto;

public interface GoogleApiOAuthAdapter {
    GoogleTokensDto exchangeAuthCodeWithToken(String authCode);
    GoogleTokensDto renewAccessAndRefreshToken(String refreshToken);
    UserDto getUserProfile(String idToken);
}
