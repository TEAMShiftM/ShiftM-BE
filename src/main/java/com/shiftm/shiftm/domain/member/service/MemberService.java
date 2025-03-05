package com.shiftm.shiftm.domain.member.service;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.domain.enums.Gender;
import com.shiftm.shiftm.domain.member.domain.enums.Role;
import com.shiftm.shiftm.domain.member.domain.enums.Status;
import com.shiftm.shiftm.domain.member.dto.request.*;
import com.shiftm.shiftm.domain.member.exception.DuplicatedEmailException;
import com.shiftm.shiftm.domain.member.exception.DuplicatedIdException;
import com.shiftm.shiftm.domain.member.exception.MemberNotFoundException;
import com.shiftm.shiftm.domain.member.exception.PasswordNotMatchException;
import com.shiftm.shiftm.domain.member.repository.MemberDao;
import com.shiftm.shiftm.domain.member.repository.MemberRepository;
import com.shiftm.shiftm.infra.email.MailSender;
import com.shiftm.shiftm.infra.redis.RedisService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberDao memberDao;
    private final RedisService redisService;
    private final MailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    // TODO : companyId 유효성 검사 추가
    @Transactional
    public Member signUp(final SignUpRequest requestDto) {
        if (memberRepository.existsById(requestDto.id())) {
            throw new DuplicatedIdException(requestDto.id());
        }

        if (memberRepository.existsByEmail(requestDto.email())) {
            throw new DuplicatedEmailException(requestDto.email());
        }

        final String password = passwordEncoder.encode(requestDto.password());
        return memberRepository.save(requestDto.toEntity(password, Role.ROLE_USER));
    }

    @Transactional
    public boolean isUniqueId(final String id) {
        return !memberRepository.existsById(id);
    }

    @Transactional
    public void sendEmailVerificationCode(final String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicatedEmailException(email);
        }

        final String verificationCode = createVerificationCode();

        redisService.saveValue("VERIFICATION_CODE:" + email, verificationCode, 1000 * 60 * 30);

        final String mailMessage = createMailMessage("ShiftM 이메일 인증 번호", "아래 인증 번호로 이메일 인증을 해주세요.", verificationCode);
        mailSender.sendMail(email, "[ShiftM] 이메일 인증 번호", mailMessage);
    }

    @Transactional
    public boolean verifyEmailCode(final VerifyEmailCodeRequest requestDto) {
        final String storedVerificationCode = redisService.getValue("VERIFICATION_CODE:" + requestDto.email());
        return storedVerificationCode.equals(requestDto.verificationCode());
    }

    @Transactional
    public void findId(final FindIdRequest requestDto) {
        final Member member = memberDao.findByEmail(requestDto.email());

        final String mailMessage = createMailMessage("ShiftM 아이디 찾기", "회원님의 아이디는 다음과 같습니다.", member.getId());
        mailSender.sendMail(requestDto.email(), "[ShiftM] 아이디 찾기", mailMessage);
    }

    @Transactional
    public void findPassword(final FindPasswordRequest requestDto) {
        final Member member = memberDao.findByEmail(requestDto.email());

        if (!member.getId().equals(requestDto.id())) {
            throw new MemberNotFoundException(requestDto.id());
        }

        final String tempPassword = generateTempPassword();
        member.setPassword(passwordEncoder.encode(tempPassword));

        final String mailMessage = createMailMessage("ShiftM 임시 비밀번호 발급", "회원님의 임시 비밀번호는 다음과 같습니다.", tempPassword);
        mailSender.sendMail(requestDto.email(), "[ShiftM] 임시 비밀번호 발급", mailMessage);
    }

    @Transactional
    public Member getProfile(final String memberId) {
        return memberDao.findById(memberId);
    }

    @Transactional
    public Member updateProfile(final String memberId, final UpdateRequest requestDto) {
        final Member member = memberDao.findById(memberId);

        member.setEmail(requestDto.email());
        member.setName(requestDto.name());
        member.setBirthDate(requestDto.birthDate());
        member.setGender(Gender.valueOf(requestDto.gender().toUpperCase()));

        return member;
    }

    @Transactional
    public void updatePassword(final String memberId, final UpdatePasswordRequest requestDto) {
        final Member member = memberDao.findById(memberId);

        if (!passwordEncoder.matches(requestDto.currentPassword(), member.getPassword())) {
            throw new PasswordNotMatchException();
        }

        member.setPassword(passwordEncoder.encode(requestDto.newPassword()));
    }

    @Transactional
    public void withdraw(final String memberId) {
        final Member member = memberDao.findById(memberId);

        member.setStatus(Status.INACTIVE);
    }

    @Transactional
    public List<Member> getAllEmployee() {
        final List<Member> memberList = memberRepository.findAll();
        return memberList;
    }

    private String createVerificationCode() {
        final Random random = new Random();
        final StringBuilder verificationCode = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            verificationCode.append(random.nextInt(10));
        }

        return verificationCode.toString();
    }

    private String createMailMessage(final String title, final String message, final String content) {
        final StringBuilder mailMessage = new StringBuilder();

        mailMessage.append("<table text-align='center' cellpadding='0' cellspacing='0' width='100%' ");
        mailMessage.append("style='max-width: 600px; background-color: #FFFFFF; border: 1px solid #DDDDDD; border-radius: 8px; margin: 20px auto; padding: 20px;'>");
        mailMessage.append("<tr>");
        mailMessage.append("<td text-align='center' style='padding: 20px;'>");
        mailMessage.append("<h2 style='color: #333333; margin: 0;'>" + title + "</h2>");
        mailMessage.append("<p style='color: #666666; font-size: 16px;'>" + message + "</p>");
        mailMessage.append("</td>");
        mailMessage.append("</tr>");
        mailMessage.append("<tr>");
        mailMessage.append("<td style='text-align: center; padding: 20px 0;'>");
        mailMessage.append("<span style='display: inline-block; font-size: 24px; color: #333333;'>" + content + "</span>");
        mailMessage.append("</td>");
        mailMessage.append("<tr>");
        mailMessage.append("<td style='padding: 20px; color: #333333; font-size: 16px;'>");
        mailMessage.append("<p style='margin: 0;'>만약 해당 메일을 요청하지 않았으면 무시해주시기 바랍니다.</p>");
        mailMessage.append("<p style='margin: 0;'>감사합니다.</p>");
        mailMessage.append("<p style='margin: 10px 0 0 0;'>Team ShiftM</p>");
        mailMessage.append("</td>");
        mailMessage.append("</tr>");
        mailMessage.append("</table>");

        return mailMessage.toString();
    }

    private String generateTempPassword() {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_-";
        final int PASSWORD_LENGTH = 8;
        final SecureRandom random = new SecureRandom();

        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }
}
