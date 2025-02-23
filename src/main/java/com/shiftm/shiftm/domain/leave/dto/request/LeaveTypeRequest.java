package com.shiftm.shiftm.domain.leave.dto.request;

import com.shiftm.shiftm.domain.leave.domain.LeaveType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LeaveTypeRequest(
        @Schema(description = "연차 유형 종류", defaultValue = "연차유급휴가")
        @NotBlank
        String name
) {
    public LeaveType toEntity(final String name) {
        return LeaveType.builder()
                .name(name)
                .build();
    }
}
