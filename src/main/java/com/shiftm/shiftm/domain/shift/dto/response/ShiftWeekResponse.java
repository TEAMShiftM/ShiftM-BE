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
            final List<Shift> shifts,
            final Set<LocalDate> holidays,
            final Set<LocalDate> leaves) {

        // Shift 데이터를 날짜별로 매핑
        final Map<LocalDate, Shift> shiftMap = createShiftMap(shifts);

        // ShiftDayResponse 객체 생성
        final List<ShiftDayResponse> shiftResponses = generateShiftDayResponses(weekStart, checkinTime, checkoutTime, shiftMap, holidays, leaves);

        return new ShiftWeekResponse(weekStart, weekEnd, shiftResponses);
    }

    // Shift를 날짜별로 매핑
    private static Map<LocalDate, Shift> createShiftMap(final List<Shift> shifts) {
        return shifts.stream()
                .collect(Collectors.toMap(
                        shift -> shift.getCheckin().getCheckinTime().toLocalDate(),
                        shift -> shift
                ));
    }

    // 주 단위로 ShiftDayResponse 객체 생성
    private static List<ShiftDayResponse> generateShiftDayResponses(
            final LocalDate weekStart,
            final LocalTime checkinTime,
            final LocalTime checkoutTime,
            final Map<LocalDate, Shift> shiftMap,
            final Set<LocalDate> holidays,
            final Set<LocalDate> leaves) {

        return IntStream.range(0, 7)
                .mapToObj(i -> {
                    final LocalDate date = weekStart.plusDays(i);
                    return ShiftDayResponse.of(date, checkinTime, checkoutTime, shiftMap.get(date), holidays, leaves);
                })
                .collect(Collectors.toList());
    }
}