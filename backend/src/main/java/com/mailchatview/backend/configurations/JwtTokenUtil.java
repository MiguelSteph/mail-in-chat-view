package com.mailchatview.backend.configurations;

import com.mailchatview.backend.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    @Value("${jwt.access-token.validity}")
    private int accessTokenValidityInSeconds;

    @Value("${jwt.refresh-token.validity}")
    private int refreshTokenValidityInSeconds;

//    private final String secretKey = "secretkeyjngjbvngjbgvbjsdgjbgjbgbjgsfbkgjbdgsbjsdgbgdjbgdsjbgdsfbjgbjgfjfdvvjgkbgjfbvsgjkbgfsdaqqfnefnrdfjvsfdjvdfnv";
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateAccessToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("picture", user.getPictureUrl());
        return doGenerateToken(claims,
                new Date(System.currentTimeMillis() + accessTokenValidityInSeconds * 1000L));
    }

    public String generateRefreshToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("scopes", Collections.singletonList(new SimpleGrantedAuthority("REFRESH_TOKEN")));
        return doGenerateToken(claims,
                new Date(System.currentTimeMillis() + refreshTokenValidityInSeconds * 1000L));
    }

    private String doGenerateToken(Claims claims, Date expirationDate) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("MailChatViewApplication")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
//                .signWith(SignatureAlgorithm.HS256, secretKey)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
