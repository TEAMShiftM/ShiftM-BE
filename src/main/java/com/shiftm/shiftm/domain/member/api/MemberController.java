package com.shiftm.shiftm.domain.member.api;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.dto.request.SignUpRequest;
import com.shiftm.shiftm.domain.member.dto.request.UpdateRequest;
import com.shiftm.shiftm.domain.member.dto.request.VerifyEmailCodeRequest;
import com.shiftm.shiftm.domain.member.dto.response.CheckResponse;
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

    @GetMapping("/check/id")
    public CheckResponse checkUniqueId(@RequestParam final String id) {
        final boolean isVerified = memberService.isUniqueId(id);
        return new CheckResponse(isVerified);
    }

    @PostMapping("/check/email")
    public void sendEmailVerificationCode(@RequestParam final String email) {
        memberService.sendEmailVerificationCode(email);
    }

    @GetMapping("/check/email/code")
    public CheckResponse verifyEmailCode(@Valid @RequestBody final VerifyEmailCodeRequest requestDto) {
        final boolean isVerified = memberService.verifyEmailCode(requestDto);
        return new CheckResponse(isVerified);
    }

    @GetMapping("/me")
    public MemberResponse getProfile(@AuthId final String memberId) {
        final Member member = memberService.getProfile(memberId);
        return new MemberResponse(member);
    }

    @PatchMapping("/me")
    public MemberResponse updateProfile(@AuthId final String memberId, @Valid @RequestBody final UpdateRequest requestDto) {
        final Member member = memberService.updateProfile(memberId, requestDto);
        return new MemberResponse(member);
    }


}
