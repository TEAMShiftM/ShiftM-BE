package com.shiftm.shiftm.domain.leave.dto.response;

import com.shiftm.shiftm.domain.leave.domain.LeaveType;

import java.util.List;
import java.util.stream.Collectors;

public record ListLeaveTypeResponse(
        List<LeaveTypeResponse> leaveTypeList
) {
    public static ListLeaveTypeResponse of(final List<LeaveType> leaveTypeList) {
        final List<LeaveTypeResponse> LeaveTypeResponseList = leaveTypeList.stream()
                .map(LeaveTypeResponse::new)
                .collect(Collectors.toList());

        return new ListLeaveTypeResponse(LeaveTypeResponseList);
    }
}