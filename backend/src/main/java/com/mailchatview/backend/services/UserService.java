package com.mailchatview.backend.services;

import com.mailchatview.backend.dtos.GoogleTokensDto;
import com.mailchatview.backend.dtos.UserDto;
import com.mailchatview.backend.entities.User;

public interface UserService {
    User createUser(UserDto userDto, GoogleTokensDto googleTokensDto);
    User findByUsername(String username);
}
