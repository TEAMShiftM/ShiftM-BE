package com.shiftm.shiftm.domain.shift.service;

import com.shiftm.shiftm.domain.company.domain.Company;
import com.shiftm.shiftm.domain.company.repository.CompanyFindDao;
import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.repository.LeaveRequestRepository;
import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.repository.MemberFindDao;
import com.shiftm.shiftm.domain.shift.domain.Shift;
import com.shiftm.shiftm.domain.shift.domain.enums.Status;
import com.shiftm.shiftm.domain.shift.dto.request.*;
import com.shiftm.shiftm.domain.shift.dto.response.ShiftWeekResponse;
import com.shiftm.shiftm.domain.shift.exception.CheckinAlreadyExistsException;
import com.shiftm.shiftm.domain.shift.exception.ShiftNotFoundException;
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

    private final ShiftRepository shiftRepository;
    private final CompanyFindDao companyFindDao;
    private final KakaoGeocodingClient geocodingClient;
    private final MemberFindDao memberFindDao;
    private final HolidayClient holidayClient;
    private final LeaveRequestRepository leaveRequestRepository;

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
        return shiftRepository.findShiftsByMemberAndCheckinTimeInRange(member, start, end);
    }

    @Transactional(readOnly = true)
    public Shift getShiftForCurrentDay(final String memberId) {
        final Member member = memberFindDao.findById(memberId);
        final LocalDate today = LocalDate.now();
        final LocalDateTime startOfDay = today.atStartOfDay();
        final LocalDateTime pivot = today.atTime(PIVOT_TIME);

        final LocalDateTime start = (LocalDateTime.now().isBefore(pivot)) ? startOfDay.minusDays(1) : startOfDay;
        final LocalDateTime end = start.plusDays(1).minusNanos(1);
        final Shift shift = shiftRepository.findShiftByMemberAndCheckinTimeInRange(member, start, end)
                .orElseThrow(() -> new ShiftNotFoundException());
        return shift;
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
        final Shift shift = findById(shiftId);
        shift.updateStatus(requestDto.status());
        return shift;
    }

    @Transactional
    public Shift updateShift(final Long shiftId, final ShiftRequest requestDto) {
        final Shift shift = findById(shiftId);
        final String address = geocodingClient.getAddress(requestDto.latitude(), requestDto.longitude());
        shift.update(requestDto.checkinTime(), requestDto.latitude(),
                requestDto.longitude(), address, requestDto.status(), requestDto.checkoutTime());
        return shift;
    }

    private void validateDuplicateCheckin(final Member member) {
        final LocalDateTime start = LocalDate.now().atStartOfDay();
        final LocalDateTime end = start.plusDays(1).minusNanos(1);
        if (shiftRepository.existsByMemberAndCheckinTimeInRange(member, start, end)) {
            throw new CheckinAlreadyExistsException();
        }
    }

    @Transactional(readOnly = true)
    public ShiftWeekResponse getWeekShifts(final String memberId) {
        final LocalDate today = LocalDate.now();
        final LocalDate weekStart = today.getDayOfWeek() == DayOfWeek.SUNDAY
                ? today.with(DayOfWeek.SUNDAY)
                : today.with(DayOfWeek.SUNDAY).minusWeeks(1); // 일요일이 아니면, 지난 주 일요일부터
        final LocalDate weekEnd = weekStart.plusDays(6);

        final Company company = companyFindDao.findFirst();
        final Member member = memberFindDao.findById(memberId);
        final Map<LocalDate, Shift> shiftMap = shiftRepository.findShiftsByMemberAndCheckinTimeInRange(
                member, weekStart.atStartOfDay(), weekEnd.atTime(23, 59)).stream()
                .collect(Collectors.toMap(shift -> shift.getCheckin().getCheckinTime().toLocalDate(), shift -> shift));
        final Map<LocalDate, Double> leaveMap = leaveRequestRepository.findApprovedLeaves(member, weekStart, weekEnd).stream()
                .flatMap(leaveRequest -> leaveRequest.getStartDate().datesUntil(leaveRequest.getEndDate().plusDays(1))
                        .map(leaveDate -> new AbstractMap.SimpleEntry<>(leaveDate, leaveRequest.getCount())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        final Set<LocalDate> holidays = new HashSet<>(holidayClient.getHolidaysBetweenDates(weekStart, weekEnd));

        return ShiftWeekResponse.of(weekStart, weekEnd, company.getCheckinTime(), company.getCheckoutTime(), shiftMap, holidays, leaveMap);
    }

    private Shift findById(final Long shiftId) {
        return shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ShiftNotFoundException());
    }

    private boolean isWithinDistance(final double latitude, final double longitude) {
        final Company company = companyFindDao.findFirst();
        final double distance = DistanceUtil.calculateDistance(company.getLatitude(), company.getLongitude(), latitude, longitude);

        return distance <= COMPANY_THRESHOLD;
    }
}
