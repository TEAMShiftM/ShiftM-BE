package com.shiftm.shiftm.domain.member.api;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.dto.request.UpdateForAdminRequest;
import com.shiftm.shiftm.domain.member.dto.response.ListMemberResponse;
import com.shiftm.shiftm.domain.member.dto.response.MemberResponse;
import com.shiftm.shiftm.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/admin/member")
@RestController
public class AdminMemberController {

    private final MemberService memberService;

    @GetMapping
    public ListMemberResponse getAllEmployee() {
        final List<Member> memberList = memberService.getAllEmployee();
        return ListMemberResponse.of(memberList);
    }

    @GetMapping("/{memberId}")
    public MemberResponse getMember(@PathVariable("memberId") final String memberId) {
        final Member member = memberService.getProfile(memberId);
        return new MemberResponse(member);
    }

    @PatchMapping("/{memberId}")
    public MemberResponse updateMember(@PathVariable("memberId") final String memberId,
                                       @Valid @RequestBody final UpdateForAdminRequest requestDto) {
        final Member member = memberService.updateMemberForAdmin(memberId, requestDto);
        return new MemberResponse(member);
    }

    @DeleteMapping("/{memberId}")
    public void withdrawMember(@PathVariable("memberId") final String memberId) {
        memberService.withdraw(memberId);
    }

    @GetMapping("/search")
    public ListMemberResponse findMemberByName(@RequestParam final String name) {
        final List<Member> memberList = memberService.findMemberByName(name);
        return ListMemberResponse.of(memberList);
    }
}
