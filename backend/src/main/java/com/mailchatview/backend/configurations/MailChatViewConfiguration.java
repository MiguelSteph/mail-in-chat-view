package com.mailchatview.backend.configurations;

import com.mailchatview.backend.util.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class MailChatViewConfiguration {

    @Value("${database.password}")
    private String databasePwd;

    @Value("${database.username}")
    private String dbUsername;

    @Value("${database.url}")
    private String dbUrl;

    @Value("${database.driver-class-name}")
    private String dbDriverClassName;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName(dbDriverClassName)
                .url(dbUrl)
                .username(dbUsername)
                .password(Utils.readSecret(databasePwd))
                .build();
    }
}
