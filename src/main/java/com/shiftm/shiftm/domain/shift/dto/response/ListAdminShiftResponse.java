package com.shiftm.shiftm.domain.shift.dto.response;

import com.shiftm.shiftm.domain.shift.domain.Shift;

import java.util.List;
import java.util.stream.Collectors;

public record ListAdminShiftResponse(
    List<AdminShiftResponse> content,
    int page,
    int size,
    int totalPages,
    Long totalElements
) {
    public static ListAdminShiftResponse of(final List<Shift> shifts,
                                  final int page, final int size, final int totalPages, final Long totalElements) {
        final List<AdminShiftResponse> content = shifts.stream()
                .map(AdminShiftResponse::new)
                .collect(Collectors.toList());

        return new ListAdminShiftResponse(content, page, size, totalPages, totalElements);
    }
}
