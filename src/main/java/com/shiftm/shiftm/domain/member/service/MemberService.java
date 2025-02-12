package com.shiftm.shiftm.domain.member.service;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.domain.enums.Role;
import com.shiftm.shiftm.domain.member.dto.request.SignUpRequest;
import com.shiftm.shiftm.domain.member.dto.response.MemberResponse;
import com.shiftm.shiftm.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberResponse signUp(final SignUpRequest requestDto) {
        String password = passwordEncoder.encode(requestDto.password());
        Member member = memberRepository.save(requestDto.toEntity(password, Role.USER));

        return new MemberResponse(member);
    }
}
