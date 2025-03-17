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
                                      final Shift shift, final Set<LocalDate> holidays, final Double leaveCount) {
        final DayOfWeek dayOfWeek = date.getDayOfWeek();
        final boolean isWeekend = (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
        final boolean isHoliday = holidays.contains(date);
        final boolean isLeave = (leaveCount != null && leaveCount > 0);
        final boolean isWorked = (shift != null);

        final ShiftType type = isWeekend ? ShiftType.WEEKEND : 
                isHoliday ? ShiftType.HOLIDAY :
                        isLeave ? (leaveCount == 1 ? ShiftType.FULL_DAY_LEAVE :
                                leaveCount == 0.5 ? ShiftType.HALF_DAY_LEAVE :
                                        leaveCount == 0.25 ? ShiftType.QUARTER_DAY_LEAVE : ShiftType.SCHEDULED_SHIFT) :
                                isWorked ? ShiftType.COMPLETED_SHIFT : ShiftType.SCHEDULED_SHIFT;

        final LocalTime startTime = (isWeekend || isHoliday || isLeave) ?
                (isLeave && leaveCount < 1 ? defaultCheckinTime : null) :
                (isWorked ? shift.getCheckin().getCheckinTime().toLocalTime() : defaultCheckinTime);
        final LocalTime endTime = (isWeekend || isHoliday || (isLeave && leaveCount == 1)) ? null :
                (isWorked ? shift.getCheckout().getCheckoutTime().toLocalTime() :
                        (isLeave && leaveCount == 0.5) ? (startTime.plusHours(4)) :
                                (isLeave && leaveCount == 0.25) ? (startTime.plusHours(6)) : null);

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