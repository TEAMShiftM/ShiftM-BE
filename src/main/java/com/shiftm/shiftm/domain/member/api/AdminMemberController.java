package com.shiftm.shiftm.domain.member.api;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.dto.response.MemberResponse;
import com.shiftm.shiftm.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/admin/member")
@RestController
public class AdminMemberController {
    private final MemberService memberService;

    public List<MemberResponse> getAllEmployee() {
        final List<Member> memberList = memberService.getAllEmployee();
        return memberList.stream()
                .map(MemberResponse::new)
                .collect(Collectors.toList());
    }
}
