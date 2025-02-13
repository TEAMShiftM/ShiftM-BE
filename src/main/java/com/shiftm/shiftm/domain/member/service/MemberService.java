package com.shiftm.shiftm.domain.member.service;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.domain.enums.Role;
import com.shiftm.shiftm.domain.member.dto.request.SignUpRequest;
import com.shiftm.shiftm.domain.member.exception.DuplicatedEmailException;
import com.shiftm.shiftm.domain.member.exception.DuplicatedIdException;
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
    public Member signUp(final SignUpRequest requestDto) {
        if (memberRepository.existsById(requestDto.id())) {
            throw new DuplicatedIdException(requestDto.id());
        }

        if (memberRepository.existsByEmail(requestDto.email())) {
            throw new DuplicatedEmailException(requestDto.email());
        }

        final String password = passwordEncoder.encode(requestDto.password());
        return memberRepository.save(requestDto.toEntity(password, Role.USER));
    }
}
