package com.shiftm.shiftm.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateForAdminRequest(
        @NotBlank
        String name,
        @NotNull
        LocalDate birthDate,
        @NotBlank
        String gender,
        LocalDate entryDate
) {
}
