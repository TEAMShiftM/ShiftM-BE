package com.shiftm.shiftm.domain.shift.dto.response;

import com.shiftm.shiftm.domain.shift.domain.Shift;

import java.util.List;
import java.util.stream.Collectors;

public record ListAfterCheckinResponse(
    List<AdminAfterCheckinResponse> content,
    int page,
    int size,
    int totalPages,
    Long totalElements
) {
    public static ListAfterCheckinResponse of(final List<Shift> shifts,
                                    final int page, final int size, final int totalPages, final Long totalElements) {
        final List<AdminAfterCheckinResponse> content = shifts.stream()
                .map(AdminAfterCheckinResponse::new)
                .collect(Collectors.toList());

        return new ListAfterCheckinResponse(content, page, size, totalPages, totalElements);
    }
}
