package com.shiftm.shiftm.domain.member.dto.request;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.domain.enums.Gender;
import com.shiftm.shiftm.domain.member.domain.enums.Role;
import com.shiftm.shiftm.domain.member.domain.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record SignUpRequest(
        @Pattern(regexp = "^[a-z0-9_-]{5,15}$")
        String id,
        @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_-]{8,20}")
        String password,
        @NotBlank
        String companyId,
        @NotBlank
        String email,
        @NotBlank
        String name,
        @NotNull
        LocalDate birthDate,
        @NotBlank
        String gender
) {
        public Member toEntity(final String password,
                               final Role role) {
                return Member.builder()
                        .id(id)
                        .password(password)
                        .email(email)
                        .name(name)
                        .birthDate(birthDate)
                        .gender(Gender.valueOf(gender.toUpperCase()))
                        .status(Status.ACTIVE)
                        .role(role)
                        .build();
        }
}
