package com.shiftm.shiftm.domain.auth.service;

import com.shiftm.shiftm.domain.auth.dto.request.LoginRequest;
import com.shiftm.shiftm.domain.auth.dto.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    public TokenResponse login(final LoginRequest loginRequest) {
        return new TokenResponse("accessToken", "refreshToken");
    }
}
