package com.shiftm.shiftm.domain.member.api;

import com.shiftm.shiftm.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/admin/member")
@RestController
public class AdminMemberController {
    private final MemberService memberService;

}
