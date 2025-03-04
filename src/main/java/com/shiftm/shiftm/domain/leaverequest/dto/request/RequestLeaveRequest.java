package com.shiftm.shiftm.domain.leaverequest.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RequestLeaveRequest(
        @NotNull
        Long leaveTypeId,
        @NotNull
        LocalDate startDate,
        @NotNull
        LocalDate endDate,
        @NotNull
        Double count
) {
}
