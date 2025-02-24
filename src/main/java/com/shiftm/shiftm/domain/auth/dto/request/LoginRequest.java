package com.shiftm.shiftm.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        String id,
        @NotBlank
        String password
) {
}
