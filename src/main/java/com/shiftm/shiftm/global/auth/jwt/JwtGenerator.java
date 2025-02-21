package com.shiftm.shiftm.global.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtGenerator {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.access-token-expiration-time}")
    private long accessTokenExpirationTime;

    @Value("${security.jwt.token.refresh-token-expiration-time}")
    private long refreshTokenExpirationTime;

    private static final String MEMBER_ROLE_CLAIM_NAME = "role";

    public String generateAccessToken(final String memberId, final String role) {
        final long now = getNow();

        return Jwts.builder()
                .subject(memberId)
                .claim(MEMBER_ROLE_CLAIM_NAME, role)
                .expiration(getExpirationTime(now, accessTokenExpirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(final String memberId) {
        final long now = getNow();

        return Jwts.builder()
                .subject(memberId)
                .expiration(getExpirationTime(now, refreshTokenExpirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    private long getNow() {
        return System.currentTimeMillis();
    }

    private Date getExpirationTime(final long now, final long expirationTime) {
        return new Date(now + expirationTime);
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
