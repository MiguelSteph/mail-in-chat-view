package com.mailchatview.authserver.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class AuthUser extends User {

    private String firstName;
    private String lastName;
    private String email;
    private String pictureUrl;

    private AuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public static class AuthUserBuilder {
        private String firstName;
        private String lastName;
        private String email;
        private String pictureUrl;
        private final String username;
        private final String password;
        private final Collection<? extends GrantedAuthority> authorities;

        public AuthUserBuilder(String username, String password, Collection<? extends GrantedAuthority> authorities) {
            this.username = username;
            this.password = password;
            this.authorities = authorities;
        }

        public AuthUserBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AuthUserBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AuthUserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public AuthUserBuilder setPictureUrl(String pictureUrl) {
            this.pictureUrl = pictureUrl;
            return this;
        }

        public AuthUser build() {
            AuthUser authUser = new AuthUser(this.username, this.password, this.authorities);
            authUser.setFirstName(this.firstName);
            authUser.setLastName(this.lastName);
            authUser.setEmail(this.email);
            authUser.setPictureUrl(this.pictureUrl);
            return authUser;
        }
    }
}
