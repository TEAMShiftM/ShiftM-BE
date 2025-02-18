package com.shiftm.shiftm.domain.auth.service;

import com.shiftm.shiftm.domain.auth.dto.request.LoginRequest;
import com.shiftm.shiftm.domain.auth.dto.response.TokenResponse;
import com.shiftm.shiftm.domain.auth.exception.InvalidPasswordException;
import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.repository.MemberDao;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final MemberDao memberDao;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TokenResponse login(final LoginRequest loginRequest) {
        authenticateMember(loginRequest.id(), loginRequest.password());

        return new TokenResponse("accessToken", "refreshToken");
    }

    private void authenticateMember(final String id, String password) {
        final Member member = memberDao.findById(id);

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new InvalidPasswordException();
        }
    }
}
