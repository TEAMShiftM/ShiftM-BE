package com.shiftm.shiftm.domain.shift.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record ShiftDayResponse(
        LocalDate date,
        String day,
        LocalTime startTime,
        LocalTime endTime,
        ShiftType type
) {
    public static ShiftDayResponse of(final LocalDate date, final String day, final LocalTime startTime,
                                      final LocalTime endTime, final ShiftType type) {
        return new ShiftDayResponse(date, day, startTime, endTime, type);
    }
}