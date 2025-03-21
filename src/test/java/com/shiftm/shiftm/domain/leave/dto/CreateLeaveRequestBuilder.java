package com.shiftm.shiftm.domain.leave.dto;

import com.shiftm.shiftm.domain.leave.dto.request.CreateLeaveRequest;

import java.time.LocalDate;
import java.util.List;

public class CreateLeaveRequestBuilder {
    public static CreateLeaveRequest build() {
        final List<String> memberIdList = List.of("shiftm1", "shiftm2", "shiftm3");
        final Long leaveTypeId = 1L;
        final LocalDate expirationDate = LocalDate.of(2025, 12, 31);
        final Double count = 10.0;

        return new CreateLeaveRequest(memberIdList, leaveTypeId, expirationDate, count);
    }
}