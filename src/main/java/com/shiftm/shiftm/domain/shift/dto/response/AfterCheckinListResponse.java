package com.shiftm.shiftm.domain.shift.dto.response;

import java.util.List;

public record AfterCheckinListResponse(
    List<AdminAfterCheckinResponse> content,
    int page,
    int size,
    int totalPages,
    Long totalElements
) {
    public AfterCheckinListResponse(final List<AdminAfterCheckinResponse> content,
                                    final int page, final int size, final int totalPages, final Long totalElements) {
        this.content = List.copyOf(content);
        this.page = page;
        this.size = size;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }
}
