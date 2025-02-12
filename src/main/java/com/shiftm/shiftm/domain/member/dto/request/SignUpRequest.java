package com.shiftm.shiftm.domain.member.dto.request;

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
        @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")
        String email,
        @NotBlank
        String name,
        @NotNull
        LocalDate birthDate,
        @NotBlank
        String gender
) {
}
