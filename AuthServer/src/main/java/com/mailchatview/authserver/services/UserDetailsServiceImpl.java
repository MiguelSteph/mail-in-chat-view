package com.mailchatview.authserver.services;

import com.mailchatview.authserver.configurations.AuthUser;
import com.mailchatview.authserver.entities.User;
import com.mailchatview.authserver.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service("userDetailsService")
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User localUser = userRepo.findByUserId(userId);

        return localUser == null ? null :
                new AuthUser.AuthUserBuilder(localUser.getUserId(), localUser.getUserId(), Collections.emptyList())
                .setLastName(localUser.getLastName())
                .setFirstName(localUser.getFirstName())
                .setEmail(localUser.getEmail())
                .setPictureUrl(localUser.getPictureUrl())
                .build();
    }
}
