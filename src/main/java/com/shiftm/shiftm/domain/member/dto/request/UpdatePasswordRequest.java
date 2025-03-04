package com.shiftm.shiftm.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdatePasswordRequest(
        @NotBlank
        String currentPassword,
        @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_-]{8,20}")
        String newPassword
) {
}
