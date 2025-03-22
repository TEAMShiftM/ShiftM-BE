package com.shiftm.shiftm.domain.leave.dto;

import com.shiftm.shiftm.domain.leave.dto.request.UpdateLeaveRequest;

import java.time.LocalDate;

public class UpdateLeaveRequestBuilder {
    public static UpdateLeaveRequest build() {
        final Long leaveTypeId = 1L;
        final LocalDate expirationDate = LocalDate.of(2025, 12, 31);
        final Double count = 10.0;
        final Double usedCount = 2.5;

        return new UpdateLeaveRequest(leaveTypeId, expirationDate, count, usedCount);
    }
}