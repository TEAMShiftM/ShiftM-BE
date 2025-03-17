package com.shiftm.shiftm.domain.shift.dto.response;

import com.shiftm.shiftm.domain.shift.domain.Shift;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record ShiftWeekResponse(
        LocalDate weekStart,
        LocalDate weekEnd,
        List<ShiftDayResponse> shifts
) {
    public static ShiftWeekResponse of(
            final LocalDate weekStart,
            final LocalDate weekEnd,
            final List<ShiftDayResponse> shifts) {
        return new ShiftWeekResponse(weekStart, weekEnd, shifts);
    }
}