package com.shiftm.shiftm.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateRequest(
        @NotBlank
        String email,
        @NotBlank
        String name,
        @NotNull
        LocalDate birthDate,
        @NotBlank
        String gender
) {
}
