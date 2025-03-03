package com.shiftm.shiftm.domain.leave.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateLeaveRequest(
        @NotNull
        Long leaveTypeId,
        @NotNull
        LocalDate expirationDate,
        @NotNull
        @Min(value = 1)
        Integer count
) {
}
