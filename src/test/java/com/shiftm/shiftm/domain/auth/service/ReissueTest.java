package com.shiftm.shiftm.domain.auth.service;

import com.shiftm.shiftm.domain.auth.dto.response.TokenResponse;
import com.shiftm.shiftm.domain.auth.exception.InvalidTokenException;
import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.domain.MemberBuilder;
import com.shiftm.shiftm.domain.member.repository.MemberDao;
import com.shiftm.shiftm.global.auth.jwt.JwtGenerator;
import com.shiftm.shiftm.global.auth.jwt.JwtValidator;
import com.shiftm.shiftm.global.error.exception.EntityNotFoundException;
import com.shiftm.shiftm.infra.redis.RedisService;
import com.shiftm.shiftm.test.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ReissueTest extends UnitTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberDao memberDao;

    @Mock
    private JwtGenerator jwtGenerator;

    @Mock
    private JwtValidator jwtValidator;

    @Mock
    private RedisService redisService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void 재발급_성공() {
        // given
        final String refreshToken = "refresh_token";
        final Member member = MemberBuilder.build();

        when(jwtValidator.getSubject(any())).thenReturn("shiftm");
        when(redisService.getValue(any())).thenReturn("refresh_token");
        when(memberDao.findById(any())).thenReturn(member);
        when(jwtGenerator.generateAccessToken(any(), any())).thenReturn("access_token");
        when(jwtGenerator.generateRefreshToken(any())).thenReturn("refresh_token");

        // when
        final TokenResponse responseDto = authService.reissue(refreshToken);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.accessToken()).isNotNull();
        assertThat(responseDto.refreshToken()).isNotNull();
    }

    @Test
    public void 재발급_실패_리프레시_토큰_오류() {
        // given
        final String refreshToken = "refresh_token";

        // when, then
        assertThrows(InvalidTokenException.class, () -> authService.reissue(refreshToken));
    }

    @Test
    public void 재발급_실패_리프레시_토큰_미존재() {
        // given
        final String refreshToken = "refresh_token";

        when(jwtValidator.getSubject(any())).thenReturn("shiftm");
        when(redisService.getValue(any())).thenThrow(EntityNotFoundException.class);

        // when, then
        assertThrows(EntityNotFoundException.class, () -> authService.reissue(refreshToken));
    }

    @Test
    public void 재발급_실패_리프레시_토큰_불일치() {
        // given
        final String refreshToken = "refresh_token";

        when(jwtValidator.getSubject(any())).thenReturn("shiftm");
        when(redisService.getValue(any())).thenReturn("refresh_token_2");

        // when, then
        assertThrows(InvalidTokenException.class, () -> authService.reissue(refreshToken));
    }
}
