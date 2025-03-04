package com.shiftm.shiftm.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record VerifyEmailCodeRequest(
        @NotBlank
        String email,
        @NotBlank
        String verificationCode
) {
}
