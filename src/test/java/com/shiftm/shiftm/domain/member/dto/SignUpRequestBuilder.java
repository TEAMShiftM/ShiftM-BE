package com.shiftm.shiftm.domain.member.dto;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.dto.request.SignUpRequest;

public class SignUpRequestBuilder {
    public static SignUpRequest build(final Member member) {
        final String companyId = "company";
        final String gender = "Female";

        return new SignUpRequest(member.getId(), member.getPassword(), companyId, member.getEmail(), member.getName(), member.getBirthDate(), gender);
    }
}
