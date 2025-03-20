package com.shiftm.shiftm.domain.shift.service;

import com.shiftm.shiftm.domain.company.domain.Company;
import com.shiftm.shiftm.domain.company.repository.CompanyFindDao;
import com.shiftm.shiftm.domain.leaverequest.repository.LeaveRequestRepository;
import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.repository.MemberFindDao;
import com.shiftm.shiftm.domain.shift.domain.Shift;
import com.shiftm.shiftm.domain.shift.domain.enums.Status;
import com.shiftm.shiftm.domain.shift.dto.request.*;
import com.shiftm.shiftm.domain.shift.dto.response.ShiftDayResponse;
import com.shiftm.shiftm.domain.shift.dto.response.ShiftType;
import com.shiftm.shiftm.domain.shift.dto.response.ShiftWeekResponse;
import com.shiftm.shiftm.domain.shift.exception.CheckinAlreadyExistsException;
import com.shiftm.shiftm.domain.shift.repository.ShiftFindDao;
import com.shiftm.shiftm.domain.shift.repository.ShiftRepository;
import com.shiftm.shiftm.global.util.DistanceUtil;
import com.shiftm.shiftm.infra.geocoding.KakaoGeocodingClient;
import com.shiftm.shiftm.infra.holiday.HolidayClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ShiftService {
    private static final LocalTime PIVOT_TIME = LocalTime.of(4, 0);
    private static final Double COMPANY_THRESHOLD = 100.0;
    private static final Double QUARTER_DAY = 0.25;
    private static final Double HALF_DAY = 0.5;
    private static final Double FULL_DAY = 1.0;

    private final ShiftRepository shiftRepository;
    private final CompanyFindDao companyFindDao;
    private final KakaoGeocodingClient geocodingClient;
    private final MemberFindDao memberFindDao;
    private final HolidayClient holidayClient;
    private final LeaveRequestRepository leaveRequestRepository;
    private final ShiftFindDao shiftFindDao;

    @Transactional
    public Shift createCheckin(final String memberId, final CheckinRequest requestDto) {
        final Member member = memberFindDao.findById(memberId);
        validateDuplicateCheckin(member);

        final boolean isNearHeadOffice = isWithinDistance(requestDto.latitude(), requestDto.longitude());
        final String address = isNearHeadOffice
                ? "본사"
                : geocodingClient.getAddress(requestDto.latitude(), requestDto.longitude());
        final Status status = isNearHeadOffice
                ? Status.AUTO_APPROVED
                : Status.PENDING;

        return shiftRepository.save(requestDto.toEntity(member, address, status));
    }

    @Transactional
    public Shift createCheckout(final String memberId, final CheckoutRequest requestDto) {
        final Shift shift = getShiftForCurrentDay(memberId);
        shift.checkout(requestDto.checkoutTime());
        return shift;
    }

    @Transactional(readOnly = true)
    public List<Shift> getShiftsInRange(final String memberId, final LocalDate startDate, final LocalDate endDate) {
        final Member member = memberFindDao.findById(memberId);
        final LocalDateTime start = (startDate != null) ? startDate.atStartOfDay() : null;
        final LocalDateTime end = (endDate != null) ? endDate.plusDays(1).atStartOfDay().minusNanos(1) : null;
        return shiftFindDao.findShiftsByMemberAndCheckinTimeInRange(member, start, end);
    }

    @Transactional(readOnly = true)
    public Shift getShiftForCurrentDay(final String memberId) {
        final Member member = memberFindDao.findById(memberId);
        final LocalDate today = LocalDate.now();
        final LocalDateTime startOfDay = today.atStartOfDay();
        final LocalDateTime pivot = today.atTime(PIVOT_TIME);

        final LocalDateTime start = (LocalDateTime.now().isBefore(pivot)) ? startOfDay.minusDays(1) : startOfDay;
        final LocalDateTime end = start.plusDays(1).minusNanos(1);

        return shiftFindDao.findShiftByMemberAndCheckinTimeInRange(member, start, end);
    }

    @Transactional(readOnly = true)
    public Page<Shift> getShifts(final Pageable pageable, final String name) {
        if (name == null || name.isEmpty()) {
            return shiftRepository.findAll(pageable);
        }
        return shiftRepository.findByName(pageable, name);
    }

    @Transactional(readOnly = true)
    public Page<Shift> getAfterCheckin(final Pageable pageable, final String name) {
        if (name == null || name.isEmpty()) {
            return shiftRepository.findByStatusExcludeOrdered(pageable, Status.AUTO_APPROVED);
        }
        return shiftRepository.findByStatusExcludeAndNameOrdered(pageable, Status.AUTO_APPROVED, name);
    }

    public Shift updateAfterCheckinStatus(final Long shiftId, final ShiftStatusRequest requestDto) {
        final Shift shift = shiftFindDao.findById(shiftId);
        shift.updateStatus(requestDto.status());
        return shift;
    }

    @Transactional
    public Shift updateShift(final Long shiftId, final ShiftRequest requestDto) {
        final Shift shift = shiftFindDao.findById(shiftId);
        final String address = geocodingClient.getAddress(requestDto.latitude(), requestDto.longitude());
        shift.update(requestDto.checkinTime(), requestDto.latitude(),
                requestDto.longitude(), address, requestDto.status(), requestDto.checkoutTime());
        return shift;
    }

    @Transactional(readOnly = true)
    public ShiftWeekResponse getWeekShifts(final String memberId) {
        final LocalDate today = LocalDate.now();
        final LocalDate weekStart = today.getDayOfWeek() == DayOfWeek.SUNDAY
                ? today.with(DayOfWeek.SUNDAY)
                : today.with(DayOfWeek.SUNDAY).minusWeeks(1);
        final LocalDate weekEnd = weekStart.plusDays(6);

        final Company company = companyFindDao.findFirst();
        final Member member = memberFindDao.findById(memberId);
        final Map<LocalDate, Shift> shiftMap = shiftFindDao.findShiftsByMemberAndCheckinTimeInRange(
                member, weekStart.atStartOfDay(), weekEnd.atTime(23, 59)).stream()
                .collect(Collectors.toMap(shift -> shift.getCheckin().getCheckinTime().toLocalDate(), shift -> shift));
        final Map<LocalDate, Double> leaveMap = leaveRequestRepository.findApprovedLeaves(member, weekStart, weekEnd).stream()
                .flatMap(leaveRequest -> leaveRequest.getStartDate().datesUntil(leaveRequest.getEndDate().plusDays(1))
                        .map(leaveDate -> new AbstractMap.SimpleEntry<>(leaveDate, leaveRequest.getCount())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        final Set<LocalDate> holidays = new HashSet<>(holidayClient.getHolidaysBetweenDates(weekStart, weekEnd));

        final List<ShiftDayResponse> shifts = weekStart.datesUntil(weekEnd.plusDays(1))
                .map(date -> {
                    final Shift shift = shiftMap.get(date);
                    final Double leaveCount = leaveMap.get(date);
                    final ShiftType type = determineShiftType(date, shift, leaveCount, holidays);
                    final LocalTime startTime = determineStartTime(type, company.getCheckinTime(), shift);
                    final LocalTime endTime = determineEndTime(type, company.getCheckoutTime(), shift, startTime);
                    final String day = getKoreanDay(date.getDayOfWeek());
                    return ShiftDayResponse.of(date, day, startTime, endTime, type);
                })
                .collect(Collectors.toList());

        return ShiftWeekResponse.of(weekStart, weekEnd, shifts);
    }

    private void validateDuplicateCheckin(final Member member) {
        final LocalDateTime start = LocalDate.now().atStartOfDay();
        final LocalDateTime end = start.plusDays(1).minusNanos(1);
        if (shiftFindDao.existsByMemberAndCheckinTimeInRange(member, start, end)) {
            throw new CheckinAlreadyExistsException();
        }
    }

    private boolean isWithinDistance(final double latitude, final double longitude) {
        final Company company = companyFindDao.findFirst();
        final double distance = DistanceUtil.calculateDistance(company.getLatitude(), company.getLongitude(), latitude, longitude);

        return distance <= COMPANY_THRESHOLD;
    }

    private ShiftType determineShiftType(final LocalDate date, final Shift shift, final Double leaveCount, final Set<LocalDate> holidays) {
        final DayOfWeek dayOfWeek = date.getDayOfWeek();

        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return ShiftType.WEEKEND;
        }

        if (holidays.contains(date)) {
            return ShiftType.HOLIDAY;
        }

        if (leaveCount != null && leaveCount > 0) {
            if (Double.compare(leaveCount, FULL_DAY) == 0) {
                return ShiftType.FULL_DAY_LEAVE;
            } else if (Double.compare(leaveCount, HALF_DAY) == 0) {
                return ShiftType.HALF_DAY_LEAVE;
            } else if (Double.compare(leaveCount, QUARTER_DAY) == 0) {
                return ShiftType.QUARTER_DAY_LEAVE;
            }
        }

        return (shift != null) ? ShiftType.COMPLETED_SHIFT : ShiftType.SCHEDULED_SHIFT;
    }

    private LocalTime determineStartTime(final ShiftType type, final LocalTime defaultCheckinTime, final Shift shift) {
        return switch (type) {
            case SCHEDULED_SHIFT, HALF_DAY_LEAVE, QUARTER_DAY_LEAVE -> defaultCheckinTime;
            case COMPLETED_SHIFT -> shift.getCheckin().getCheckinTime().toLocalTime();
            default -> null;
        };
    }

    private LocalTime determineEndTime(final ShiftType type, final LocalTime defaultCheckoutTime, final Shift shift, final LocalTime startTime) {
        return switch (type) {
            case WEEKEND, HOLIDAY, FULL_DAY_LEAVE -> null;
            case HALF_DAY_LEAVE -> startTime.plusHours(4);
            case QUARTER_DAY_LEAVE -> startTime.plusHours(6);
            case COMPLETED_SHIFT -> shift.getCheckout().getCheckoutTime().toLocalTime();
            case SCHEDULED_SHIFT -> defaultCheckoutTime;
        };
    }

    private String getKoreanDay(final DayOfWeek day) {
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
