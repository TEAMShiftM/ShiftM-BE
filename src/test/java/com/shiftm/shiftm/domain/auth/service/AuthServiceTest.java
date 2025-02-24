package com.shiftm.shiftm.domain.auth.service;

import com.shiftm.shiftm.domain.auth.dto.LoginRequestBuilder;
import com.shiftm.shiftm.domain.auth.dto.request.LoginRequest;
import com.shiftm.shiftm.domain.auth.dto.response.TokenResponse;
import com.shiftm.shiftm.domain.auth.exception.InvalidPasswordException;
import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.domain.MemberBuilder;
import com.shiftm.shiftm.domain.member.exception.MemberNotFoundException;
import com.shiftm.shiftm.domain.member.repository.MemberDao;
import com.shiftm.shiftm.global.auth.jwt.JwtGenerator;
import com.shiftm.shiftm.infra.redis.RedisService;
import com.shiftm.shiftm.test.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthServiceTest extends UnitTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberDao memberDao;

    @Mock
    private JwtGenerator jwtGenerator;

    @Mock
    private RedisService redisService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void 로그인_성공() {
        // given
        final LoginRequest requestDto = LoginRequestBuilder.build();
        final Member member = MemberBuilder.build();

        when(memberDao.findById(any())).thenReturn(member);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtGenerator.generateAccessToken(any(), any())).thenReturn("access_token");
        when(jwtGenerator.generateRefreshToken(any())).thenReturn("refresh_token");

        // when
        final TokenResponse responseDto = authService.login(requestDto);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.accessToken()).isNotNull();
        assertThat(responseDto.refreshToken()).isNotNull();
    }

    @Test
    public void 로그인_실패_회원_없음() {
        // given
        final LoginRequest requestDto = LoginRequestBuilder.build();

        when(memberDao.findById(any())).thenThrow(MemberNotFoundException.class);

        // when, then
        assertThrows(MemberNotFoundException.class, () -> authService.login(requestDto));
    }

    @Test
    public void 로그인_실패_비밀번호_불일치() {
        // given
        final LoginRequest requestDto = LoginRequestBuilder.build();
        final Member member = MemberBuilder.build();

        when(memberDao.findById(any())).thenReturn(member);
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        // when, then
        assertThrows(InvalidPasswordException.class, () -> authService.login(requestDto));
    }
}
