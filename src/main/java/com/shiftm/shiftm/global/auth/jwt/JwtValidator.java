package com.shiftm.shiftm.global.auth.jwt;

import com.shiftm.shiftm.domain.auth.exception.InvalidBearerPrefixException;
import com.shiftm.shiftm.domain.auth.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtValidator {
    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    private static final String BEARER = "Bearer ";

    public void validateToken(final String token) {
        if (token == null) {
            throw new InvalidTokenException();
        }

        try {
            parseToken(token);
        } catch (JwtException e) {
            throw new InvalidTokenException();
        }
    }

    public String getSubject(final String token) {
        return parseToken(token).getSubject();
    }

    private Claims parseToken(final String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(getToken(token))
                .getPayload();
    }

    public String getToken(final String token) {
        if (!token.startsWith(BEARER)) {
            throw new InvalidBearerPrefixException();
        }

        return token.substring(BEARER.length());
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
