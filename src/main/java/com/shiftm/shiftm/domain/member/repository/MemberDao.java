package com.shiftm.shiftm.domain.member.repository;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberDao {
    private final MemberRepository memberRepository;

    public Member findById(final String id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }
}
