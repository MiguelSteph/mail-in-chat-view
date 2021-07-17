package com.mailchatview.backend.rest;

import com.mailchatview.backend.configurations.JwtTokenUtil;
import com.mailchatview.backend.dtos.GoogleClientInfo;
import com.mailchatview.backend.dtos.GoogleTokensDto;
import com.mailchatview.backend.dtos.TokenDto;
import com.mailchatview.backend.dtos.UserDto;
import com.mailchatview.backend.entities.User;
import com.mailchatview.backend.exceptions.ExpiredTokenException;
import com.mailchatview.backend.exceptions.InvalidTokenException;
import com.mailchatview.backend.services.UserService;
import com.mailchatview.backend.services.mail.GoogleApiOAuthAdapter;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/public")
public class UserResource {

    private final GoogleApiOAuthAdapter googleApiOAuthAdapter;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping("/google/client/info")
    public GoogleClientInfo googleClientInfo() {
        return googleApiOAuthAdapter.getGoogleClientInfo();
    }

    @PostMapping("/login")
    public TokenDto login(@RequestBody String authCode) {
        GoogleTokensDto googleTokensDto = googleApiOAuthAdapter.exchangeAuthCodeWithToken(
                URLDecoder.decode(authCode, StandardCharsets.UTF_8));
        UserDto userDto = googleApiOAuthAdapter.getUserProfile(googleTokensDto.getIdToken());
        User user = userService.createUser(userDto, googleTokensDto);
        return createTokens(user);
    }

    @PostMapping("/auth/token")
    public TokenDto renewAccessToken(@RequestBody String bearerToken) {
        bearerToken = URLDecoder.decode(bearerToken, StandardCharsets.UTF_8);
        if (jwtTokenUtil.isTokenExpired(bearerToken)) {
            throw new ExpiredTokenException("Refresh toke expired. Please login again");
        }
        String username = jwtTokenUtil.getUsernameFromToken(bearerToken);
        User currentUser = userService.findByUsername(username);
        if (currentUser == null) {
            throw new InvalidTokenException("Provided refresh token is invalid");
        }
        return createTokens(currentUser);
    }

    private TokenDto createTokens(User user) {
        TokenDto tokenDto = new TokenDto();
        tokenDto.setAccessToken(jwtTokenUtil.generateAccessToken(user));
        tokenDto.setRefreshToken(jwtTokenUtil.generateRefreshToken(user));
        return tokenDto;
    }
}
