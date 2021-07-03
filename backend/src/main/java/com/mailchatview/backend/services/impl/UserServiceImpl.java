package com.mailchatview.backend.services.impl;

import com.mailchatview.backend.dtos.GoogleTokensDto;
import com.mailchatview.backend.dtos.UserDto;
import com.mailchatview.backend.entities.User;
import com.mailchatview.backend.repo.UserRepo;
import com.mailchatview.backend.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Override
    public User findByUsername(String username) {
        return userRepo.findByEmail(username).orElse(null);
    }

    @Override
    public User createUser(UserDto userDto, GoogleTokensDto googleTokensDto) {
        Optional<User> existingUserOpt = userRepo.findByUserId(userDto.getUserId());

        if (existingUserOpt.isPresent()) {
            return existingUserOpt.get();
        }

        User user = new User();
        user.setUserId(userDto.getUserId());
        user.setFirstName(userDto.getName());
        user.setLastName(userDto.getFamilyName());
        user.setEmail(userDto.getEmail());
        user.setPictureUrl(userDto.getPictureUrl());
        user.setAccessToken(googleTokensDto.getAccessToken());
        user.setRefreshToken(googleTokensDto.getRefreshToken());
        user.setTokenExpirationTimestamp(googleTokensDto.getAccessTokenExpiredTimestamp());
        user.setCreatedTime(LocalDateTime.now());

        return userRepo.save(user);
    }
}
