package com.shiftm.shiftm.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record FindIdRequest(
        @NotBlank
        String email
) {
}
