package com.shiftm.shiftm.domain.leaverequest.dto.response;

import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.domain.enums.Status;

import java.time.LocalDate;

public record LeaveRequestResponse(
        Long leaveRequestId,
        String memberName,
        LocalDate startDate,
        LocalDate endDate,
        Double count,
        Status status
) {
    public LeaveRequestResponse(final LeaveRequest leaveRequest) {
        this(leaveRequest.getId(), leaveRequest.getMember().getName(), leaveRequest.getStartDate(), leaveRequest.getEndDate(), leaveRequest.getCount(), leaveRequest.getStatus());
    }
}
