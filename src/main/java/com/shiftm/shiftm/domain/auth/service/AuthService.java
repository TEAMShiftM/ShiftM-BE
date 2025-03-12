package com.shiftm.shiftm.domain.auth.service;

import com.shiftm.shiftm.domain.auth.dto.request.LoginRequest;
import com.shiftm.shiftm.domain.auth.dto.response.TokenResponse;
import com.shiftm.shiftm.domain.auth.exception.InvalidPasswordException;
import com.shiftm.shiftm.domain.auth.exception.InvalidTokenException;
import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.repository.MemberFindDao;
import com.shiftm.shiftm.global.auth.jwt.JwtGenerator;
import com.shiftm.shiftm.global.auth.jwt.JwtValidator;
import com.shiftm.shiftm.infra.redis.RedisService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final MemberFindDao memberFindDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final JwtValidator jwtValidator;
    private final RedisService redisService;

    @Value("${security.jwt.token.refresh-token-expiration-time}")
    private long refreshTokenExpirationTime;

    @Transactional
    public TokenResponse login(final LoginRequest loginRequest) {
        authenticateMember(loginRequest.id(), loginRequest.password());

        final TokenResponse tokenResponse = generateToken(loginRequest.id());

        saveRefreshToken(loginRequest.id(), tokenResponse.refreshToken());

        return tokenResponse;
    }

    @Transactional
    public TokenResponse reissue(final String refreshToken) {
        validateRefreshToken(refreshToken);

        final String memberId = jwtValidator.getSubject(refreshToken);
        final TokenResponse tokenResponse = generateToken(memberId);

        saveRefreshToken(memberId, tokenResponse.refreshToken());

        return tokenResponse;
    }

    @Transactional
    public void logout(final String memberId) {
        deleteRefreshToken(memberId);
    }

    private void authenticateMember(final String id, final String password) {
        final Member member = memberFindDao.findById(id);

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new InvalidPasswordException();
        }
    }

    private TokenResponse generateToken(final String id) {
        final Member member = memberFindDao.findById(id);

        final String accessToken = jwtGenerator.generateAccessToken(member.getId(), member.getRole().name());
        final String refreshToken = jwtGenerator.generateRefreshToken(member.getId());

        return new TokenResponse(accessToken, refreshToken);
    }

    private void saveRefreshToken(final String memberId, final String refreshToken) {
        final String key = "REFRESH_TOKEN:" + memberId;
        redisService.saveValue(key, refreshToken, refreshTokenExpirationTime);
    }

    private void validateRefreshToken(final String refreshToken) {
        jwtValidator.validateToken(refreshToken);

        final String memberId = jwtValidator.getSubject(refreshToken);

        final String storedRefreshToken = redisService.getValue(memberId);

        if (!jwtValidator.getToken(refreshToken).equals(storedRefreshToken)) {
            throw new InvalidTokenException();
        }
    }

    private void deleteRefreshToken(final String memberId) {
        final String key = "REFRESH_TOKEN:" + memberId;
        redisService.deleteValue(key);
    }
}
