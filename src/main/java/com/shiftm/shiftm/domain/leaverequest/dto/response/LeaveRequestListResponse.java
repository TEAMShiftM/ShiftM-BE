package com.shiftm.shiftm.domain.leaverequest.dto.response;

import java.util.List;

public record LeaveRequestListResponse(
        List<LeaveRequestResponse> content,
        int page,
        int size,
        int totalPages,
        Long totalElements
) {
    public LeaveRequestListResponse(final List<LeaveRequestResponse> content, final int page, final int size, final int totalPages,
                                    final Long totalElements) {
        this.content = List.copyOf(content);
        this.page = page;
        this.size = size;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }
}
