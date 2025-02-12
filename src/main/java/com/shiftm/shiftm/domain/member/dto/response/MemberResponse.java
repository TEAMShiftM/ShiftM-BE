package com.shiftm.shiftm.domain.member.dto.response;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.domain.enums.Gender;

import java.time.LocalDate;

public record MemberResponse(
        String id,
        String email,
        String name,
        LocalDate birthDate,
        Gender gender
) {
    public MemberResponse(final Member member) {
        this(member.getId(), member.getEmail(), member.getName(), member.getBirthDate(), member.getGender());
    }
}
