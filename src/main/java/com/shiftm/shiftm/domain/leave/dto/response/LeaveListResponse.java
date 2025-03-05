package com.shiftm.shiftm.domain.leave.dto.response;

import java.util.List;

public record LeaveListResponse(
        List<LeaveResponse> content,
        int page,
        int size,
        int totalPages,
        Long totalElements
) {
    public LeaveListResponse(final List<LeaveResponse> content, final int page, final int size, final int totalPages,
                             final Long totalElements) {
        this.content = List.copyOf(content);
        this.page = page;
        this.size = size;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }
}
