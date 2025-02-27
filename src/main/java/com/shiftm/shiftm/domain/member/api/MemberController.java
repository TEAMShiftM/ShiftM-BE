package com.shiftm.shiftm.domain.member.api;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.dto.request.SignUpRequest;
import com.shiftm.shiftm.domain.member.dto.response.MemberResponse;
import com.shiftm.shiftm.domain.member.service.MemberService;
import com.shiftm.shiftm.global.auth.annotation.AuthId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public MemberResponse signUp(@Valid @RequestBody final SignUpRequest requestDto) {
        final Member member = memberService.signUp(requestDto);
        return new MemberResponse(member);
    }

    @GetMapping("/me")
    public MemberResponse getProfile(@AuthId final String memberId) {
        final Member member = memberService.getProfile(memberId);
        return new MemberResponse(member);
    }
}
