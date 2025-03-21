package com.shiftm.shiftm.domain.leave.dto.response;

import com.shiftm.shiftm.domain.leave.domain.Leave;

import java.util.List;
import java.util.stream.Collectors;

public record ListAdminLeaveCreateResponse(
        List<AdminLeaveResponse> leaveList
) {
    public static ListAdminLeaveCreateResponse of(final List<Leave> leaveList) {
        final List<AdminLeaveResponse> adminLeaveResponseList = leaveList.stream()
                .map(AdminLeaveResponse::new)
                .collect(Collectors.toList());

        return new ListAdminLeaveCreateResponse(adminLeaveResponseList);
    }
}