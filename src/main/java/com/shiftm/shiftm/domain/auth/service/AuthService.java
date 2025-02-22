package com.shiftm.shiftm.domain.auth.service;

import com.shiftm.shiftm.domain.auth.dto.request.LoginRequest;
import com.shiftm.shiftm.domain.auth.dto.response.TokenResponse;
import com.shiftm.shiftm.domain.auth.exception.InvalidPasswordException;
import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.repository.MemberDao;
import com.shiftm.shiftm.global.auth.jwt.JwtGenerator;
import com.shiftm.shiftm.infra.redis.RedisService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final MemberDao memberDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final RedisService redisService;

    @Value("${security.jwt.token.refresh-token-expiration-time}")
    private long refreshTokenExpirationTime;

    @Transactional
    public TokenResponse login(final LoginRequest loginRequest) {
        authenticateMember(loginRequest.id(), loginRequest.password());

        final TokenResponse tokenResponse = generateToken(loginRequest.id());

        saveRefreshToken(loginRequest.id(), tokenResponse.refreshToken());
        return new TokenResponse("accessToken", "refreshToken");
    }

    private void authenticateMember(final String id, final String password) {
        final Member member = memberDao.findById(id);

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new InvalidPasswordException();
        }
    }

    private TokenResponse generateToken(final String id) {
        final Member member = memberDao.findById(id);

        final String accessToken = jwtGenerator.generateAccessToken(member.getId(), member.getRole().name());
        final String refreshToken = jwtGenerator.generateRefreshToken(member.getId());

        return new TokenResponse(accessToken, refreshToken);
    }

    private void saveRefreshToken(final String memberId, final String refreshToken) {
        final String key = "REFRESH_TOKEN:" + memberId;
        redisService.saveValue(memberId, refreshToken, refreshTokenExpirationTime);
    }
}
