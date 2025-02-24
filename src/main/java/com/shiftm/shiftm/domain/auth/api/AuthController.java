package com.shiftm.shiftm.domain.auth.api;

import com.shiftm.shiftm.domain.auth.dto.request.LoginRequest;
import com.shiftm.shiftm.domain.auth.dto.response.TokenResponse;
import com.shiftm.shiftm.domain.auth.service.AuthService;
import com.shiftm.shiftm.global.auth.annotation.AuthId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody final LoginRequest requestDto) {
        return authService.login(requestDto);
    }

    @PostMapping("/reissue")
    public TokenResponse reissue(@RequestHeader("Authorization") final String refreshToken) {
        return authService.reissue(refreshToken);
    }

    @PostMapping("/logout")
    public void logout(@AuthId final String memberId) {
        authService.logout(memberId);
    }
}
