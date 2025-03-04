package com.shiftm.shiftm.domain.leave.dto.response;

import java.time.LocalDate;

public record LeaveResponse(
        String memberId,
        String memberName,
        String leaveType,
        LocalDate expirationDate,
        Integer count
) {
}