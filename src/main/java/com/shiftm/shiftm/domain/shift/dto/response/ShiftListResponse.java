package com.shiftm.shiftm.domain.shift.dto.response;

import java.util.List;

public record ShiftListResponse(
    List<ShiftResponse> shifts
) {
    public ShiftListResponse(final List<ShiftResponse> shifts) {
        this.shifts = List.copyOf(shifts);
    }
}
