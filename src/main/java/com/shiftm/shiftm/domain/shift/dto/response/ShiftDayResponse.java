package com.shiftm.shiftm.domain.shift.dto.response;

import com.shiftm.shiftm.domain.shift.domain.Shift;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public record ShiftDayResponse(
        LocalDate date,
        String day,
        LocalTime startTime,
        LocalTime endTime,
        ShiftType type) {

    public static ShiftDayResponse of(final LocalDate date, final LocalTime defaultCheckinTime, final LocalTime defaultCheckoutTime,
                                      final Shift shift, final Set<LocalDate> holidays, Set<LocalDate> leaves) {
        final DayOfWeek dayOfWeek = date.getDayOfWeek();
        final boolean isWeekend = (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
        final boolean isHoliday = holidays.contains(date);
        final boolean isLeave = leaves.contains(date);
        final boolean isWorked = (shift != null);

        final ShiftType type = isWeekend ? ShiftType.WEEKEND : 
                isHoliday ? ShiftType.HOLIDAY : 
                        isLeave ? ShiftType.LEAVE :
                                isWorked ? ShiftType.COMPLETED_SHIFT : ShiftType.SCHEDULED_SHIFT;

        final LocalTime startTime = (isWeekend || isHoliday || isLeave) ? null :
                (isWorked ? shift.getCheckin().getCheckinTime().toLocalTime() : defaultCheckinTime);
        final LocalTime endTime = (isWeekend || isHoliday || isLeave) ? null :
                (isWorked ? shift.getCheckout().getCheckoutTime().toLocalTime() : defaultCheckoutTime);

        return new ShiftDayResponse(date, getKoreanDay(dayOfWeek), startTime, endTime, type);
    }

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