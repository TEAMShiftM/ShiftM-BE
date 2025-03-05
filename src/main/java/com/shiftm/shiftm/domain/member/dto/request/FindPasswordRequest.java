package com.shiftm.shiftm.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FindPasswordRequest(
        @NotBlank
        String id,
        @NotBlank
        String email
) {
}
