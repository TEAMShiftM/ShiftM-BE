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
        List<ShiftDayResponse> shifts) {

    public static ShiftWeekResponse of(
            final LocalDate weekStart,
            final LocalDate weekEnd,
            final LocalTime checkinTime,
            final LocalTime checkoutTime,
            final Map<LocalDate, Shift> shiftMap,
            final Set<LocalDate> holidays,
            final Map<LocalDate, Double> leaveMap) {

        return new ShiftWeekResponse(weekStart, weekEnd,
                IntStream.range(0, 7)
                .mapToObj(i -> {
                    final LocalDate date = weekStart.plusDays(i);
                    return ShiftDayResponse.of(date, checkinTime, checkoutTime, shiftMap.get(date), holidays, leaveMap.get(date));
                })
                .collect(Collectors.toList()));
    }
}