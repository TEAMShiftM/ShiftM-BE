package com.shiftm.shiftm.domain.member.domain;

import com.shiftm.shiftm.domain.member.domain.enums.Gender;
import com.shiftm.shiftm.domain.member.domain.enums.Role;
import com.shiftm.shiftm.domain.member.domain.enums.Status;

import java.time.LocalDate;

public class MemberBuilder {
    public static Member build() {
        final String id = "shiftm";
        final String password = "Password!0";
        final String email = "shiftm@gmail.com";
        final String name = "김사원";
        final LocalDate birthDate = LocalDate.of(2000, 1, 1);
        final Gender gender = Gender.FEMALE;

        {
            return Member.builder()
                    .id(id)
                    .password(password)
                    .email(email)
                    .name(name)
                    .birthDate(birthDate)
                    .gender(gender)
                    .status(Status.ACTIVE)
                    .role(Role.ROLE_USER)
                    .build();
        }
    }
}
