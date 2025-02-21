package com.shiftm.shiftm.domain.auth.service;

import com.shiftm.shiftm.domain.auth.dto.request.LoginRequest;
import com.shiftm.shiftm.domain.auth.dto.response.TokenResponse;
import com.shiftm.shiftm.domain.auth.exception.InvalidPasswordException;
import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.repository.MemberDao;
import com.shiftm.shiftm.global.auth.jwt.JwtGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final MemberDao memberDao;
    private final JwtGenerator jwtGenerator;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TokenResponse login(final LoginRequest loginRequest) {
        authenticateMember(loginRequest.id(), loginRequest.password());

        final TokenResponse tokenResponse = generateToken(loginRequest.id());

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
}
