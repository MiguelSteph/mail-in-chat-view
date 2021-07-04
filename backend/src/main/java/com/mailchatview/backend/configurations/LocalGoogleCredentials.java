package com.mailchatview.backend.configurations;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@PropertySource("classpath:google-credentials.properties")
@ConfigurationProperties(prefix = "web")
@Data
public class LocalGoogleCredentials {

    @Value("${google.web.id}")
    private String id;
    @Value("${google.web.secret}")
    private String secret;

    private String projectId;
    private String authUri;
    private String tokenUri;
    private String authProviderX509CertUrl;
    private List<String> redirectUris;
    private List<String> javascriptOrigins;
}
