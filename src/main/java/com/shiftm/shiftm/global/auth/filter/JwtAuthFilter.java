package com.shiftm.shiftm.global.auth.filter;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.repository.MemberDao;
import com.shiftm.shiftm.global.auth.jwt.JwtValidator;
import com.shiftm.shiftm.infra.redis.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final MemberDao memberDao;
    private final JwtValidator jwtValidator;
    private final RedisService redisService;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String accessToken = getAccessToken(request);

        authenticate(accessToken);

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(final HttpServletRequest request) {
        final String accessToken = request.getHeader("Authorization");

        jwtValidator.validateToken(accessToken);

        return accessToken;
    }

    private void authenticate(final String accessToken) {
        final String memberId = jwtValidator.getSubject(accessToken);
        final String storedRefreshToken = redisService.getValue(memberId);
        final Member member = memberDao.findById(memberId);

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            final UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(
                    member.getId(),
                    null,
                    Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())));

            SecurityContextHolder.getContext()
                    .setAuthentication(userAuth);
        }
    }
}
