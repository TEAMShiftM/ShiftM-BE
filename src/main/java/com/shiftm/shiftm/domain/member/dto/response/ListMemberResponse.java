package com.shiftm.shiftm.domain.member.dto.response;

import com.shiftm.shiftm.domain.member.domain.Member;

import java.util.List;
import java.util.stream.Collectors;

public record ListMemberResponse(
        List<MemberResponse> memberList
) {
    public static ListMemberResponse of(final List<Member> memberList) {
        final List<MemberResponse> memberResponseList = memberList.stream()
                .map(MemberResponse::new)
                .collect(Collectors.toList());

        return new ListMemberResponse(memberResponseList);
    }
}
