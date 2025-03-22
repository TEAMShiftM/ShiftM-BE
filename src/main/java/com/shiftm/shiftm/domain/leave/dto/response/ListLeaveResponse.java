package com.shiftm.shiftm.domain.leave.dto.response;

import com.shiftm.shiftm.domain.leave.domain.Leave;

import java.util.List;
import java.util.stream.Collectors;

public record ListLeaveResponse(
        List<LeaveResponse> content,
        int page,
        int size,
        int totalPages,
        Long totalElements
) {
    public static ListLeaveResponse of(final List<Leave> leaveList, final int page, final int size,
                                       final int totalPages, final Long totalElements) {
        final List<LeaveResponse> content = leaveList.stream()
                .map(LeaveResponse::new)
                .collect(Collectors.toList());

        return new ListLeaveResponse(content, (leaveList.isEmpty()) ? page : page + 1, size, totalPages, totalElements);
    }
}
