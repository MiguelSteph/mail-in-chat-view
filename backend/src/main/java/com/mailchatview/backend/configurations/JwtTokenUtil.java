package com.mailchatview.backend.configurations;

import com.mailchatview.backend.entities.Jwtsecret;
import com.mailchatview.backend.entities.User;
import com.mailchatview.backend.repo.JwtsecretRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
    private static final String SECRET_NAME = "EasyReadJwtSecret";

    @Value("${jwt.access-token.validity}")
    private int accessTokenValidityInSeconds;

    @Value("${jwt.refresh-token.validity}")
    private int refreshTokenValidityInSeconds;

    @Autowired
    private JwtsecretRepo jwtsecretRepo;

    private SecretKey secretKey;

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
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey getSecretKey() {
        if (this.secretKey == null) {
            final Optional<Jwtsecret> jwtsecretOpt = jwtsecretRepo.findBySecretName(SECRET_NAME);
            if (jwtsecretOpt.isPresent()) {
                this.secretKey = secretFromString(jwtsecretOpt.get());
            } else {
                this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
                Jwtsecret newJwtsecret = new Jwtsecret();
                newJwtsecret.setSecretName(SECRET_NAME);
                newJwtsecret.setSecretAlgoName(SignatureAlgorithm.HS512.name());
                newJwtsecret.setSecretContent(secretToString(this.secretKey));
                jwtsecretRepo.save(newJwtsecret);
            }
        }
        return this.secretKey;
    }

    private SecretKey secretFromString(Jwtsecret jwtsecret) {
        byte[] decodedKey = Base64.getDecoder().decode(jwtsecret.getSecretContent());
        return Keys.hmacShaKeyFor(decodedKey);
    }

    private String secretToString(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
