package com.shiftm.shiftm.domain.member.service;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.domain.MemberBuilder;
import com.shiftm.shiftm.domain.member.dto.SignUpRequestBuilder;
import com.shiftm.shiftm.domain.member.dto.request.SignUpRequest;
import com.shiftm.shiftm.domain.member.exception.DuplicatedEmailException;
import com.shiftm.shiftm.domain.member.exception.DuplicatedIdException;
import com.shiftm.shiftm.domain.member.repository.MemberDao;
import com.shiftm.shiftm.domain.member.repository.MemberRepository;
import com.shiftm.shiftm.test.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MemberServiceTest extends UnitTest {
    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberDao memberDao;

    @Spy
    private PasswordEncoder passwordEncoder;
    private Member member;

    @BeforeEach
    public void setUp() throws Exception {
        member = MemberBuilder.build();
    }

    @Test
    public void 회원가입_성공() {
        // given
        final SignUpRequest requestDto = SignUpRequestBuilder.build(member);

        when(memberRepository.existsById(any())).thenReturn(false);
        when(memberRepository.existsByEmail(any())).thenReturn(false);
        when(memberRepository.save(any())).thenReturn(member);

        // when
        final Member signUpMember = memberService.signUp(requestDto);

        // then
        assertThat(signUpMember).isNotNull();
        assertThat(signUpMember.getId()).isEqualTo(member.getId());
        assertThat(signUpMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(signUpMember.getName()).isEqualTo(member.getName());
        assertThat(signUpMember.getBirthDate()).isEqualTo(member.getBirthDate());
        assertThat(signUpMember.getGender()).isEqualTo(member.getGender());
    }

    @Test
    public void 회원가입_실패_아이디_중복() {
        // given
        final SignUpRequest requestDto = SignUpRequestBuilder.build(member);

        when(memberRepository.existsById(any())).thenReturn(true);

        // when, then
        assertThrows(DuplicatedIdException.class, () -> memberService.signUp(requestDto));
    }

    @Test
    public void 회원가입_실패_이메일_중복() {
        // given
        final SignUpRequest requestDto = SignUpRequestBuilder.build(member);

        when(memberRepository.existsById(any())).thenReturn(false);
        when(memberRepository.existsByEmail(any())).thenReturn(true);

        // when, then
        assertThrows(DuplicatedEmailException.class, () -> memberService.signUp(requestDto));
    }

    @Test
    public void 프로필_조회_성공() {
        // given
        when(memberDao.findById(any())).thenReturn(member);

        // when
        final Member profileMember = memberService.getProfile(member.getId());

        // then
        assertThat(profileMember).isNotNull();
        assertThat(profileMember.getId()).isEqualTo(member.getId());
        assertThat(profileMember.getEmail()).isEqualTo(member.getEmail());
        assertThat(profileMember.getName()).isEqualTo(member.getName());
        assertThat(profileMember.getBirthDate()).isEqualTo(member.getBirthDate());
        assertThat(profileMember.getGender()).isEqualTo(member.getGender());
    }
}
