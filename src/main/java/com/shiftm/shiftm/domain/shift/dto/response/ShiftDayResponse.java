package com.shiftm.shiftm.domain.shift.dto.response;

import com.shiftm.shiftm.domain.shift.domain.Shift;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public record ShiftDayResponse(
        String day,
        LocalTime startTime,
        LocalTime endTime) {

    // 날짜별로 ShiftDayResponse 객체 생성
    public static ShiftDayResponse of(final LocalDate date, final LocalTime checkinTime, final LocalTime checkoutTime,
                                      final Shift shift, final Set<LocalDate> holidays, Set<LocalDate> leaves) {
        final DayOfWeek dayOfWeek = date.getDayOfWeek();
        final boolean isHoliday = holidays.contains(date);
        final boolean isLeave = leaves.contains(date);
        final boolean isWeekend = (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);

        if (isWeekend || isHoliday || isLeave) {
            return new ShiftDayResponse(getKoreanDay(dayOfWeek), null, null);
        }

        if (shift != null) {
            return new ShiftDayResponse(getKoreanDay(dayOfWeek),
                    shift.getCheckin().getCheckinTime().toLocalTime(),
                    shift.getCheckout().getCheckoutTime().toLocalTime());
        }

        return new ShiftDayResponse(getKoreanDay(dayOfWeek), checkinTime, checkoutTime);
    }

    // 요일을 한글로 반환
    private static String getKoreanDay(final DayOfWeek day) {
        return switch (day) {
            case SUNDAY -> "일";
            case MONDAY -> "월";
            case TUESDAY -> "화";
            case WEDNESDAY -> "수";
            case THURSDAY -> "목";
            case FRIDAY -> "금";
            case SATURDAY -> "토";
        };
    }
}