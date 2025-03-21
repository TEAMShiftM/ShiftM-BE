package com.shiftm.shiftm.domain.leave.dto.response;

import com.shiftm.shiftm.domain.leave.domain.Leave;

import java.util.List;
import java.util.stream.Collectors;

public record ListAdminLeaveResponse(
        List<AdminLeaveResponse> content,
        int page,
        int size,
        int totalPages,
        Long totalElements
) {
    public static ListAdminLeaveResponse of(final List<Leave> leaveList, final int page, final int size,
                                            final int totalPages, final Long totalElements) {
        final List<AdminLeaveResponse> content = leaveList.stream()
                .map(AdminLeaveResponse::new)
                .collect(Collectors.toList());

        return new ListAdminLeaveResponse(content, (leaveList.isEmpty()) ? page : page + 1, size, totalPages, totalElements);
    }
}