package com.shiftm.shiftm.domain.shift.service;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.repository.MemberDao;
import com.shiftm.shiftm.domain.shift.domain.Shift;
import com.shiftm.shiftm.domain.shift.dto.request.AfterCheckinRequest;
import com.shiftm.shiftm.domain.shift.dto.request.CheckinRequest;
import com.shiftm.shiftm.domain.shift.dto.request.CheckoutRequest;
import com.shiftm.shiftm.domain.shift.dto.response.ShiftResponse;
import com.shiftm.shiftm.domain.shift.exception.CheckinAlreadyExistsException;
import com.shiftm.shiftm.domain.shift.exception.ShiftNotFoundException;
import com.shiftm.shiftm.domain.shift.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final MemberDao memberDao;

    @Transactional
    public Shift createCheckin(final String memberId, final CheckinRequest requestDto) {
        final Member member = memberDao.findById(memberId);
        checkDuplicateCheckin(member);
        final Shift shift = requestDto.toEntity(member);
        return shiftRepository.save(shift);
    }

    @Transactional
    public Shift createAfterCheckin(final String memberId, final AfterCheckinRequest requestDto) {
        final Member member = memberDao.findById(memberId);
        checkDuplicateCheckin(member);
        final Shift shift = requestDto.toEntity(member);
        return shiftRepository.save(shift);
    }

    @Transactional
    public Shift createCheckout(final String memberId, final CheckoutRequest requestDto) {
        final Shift shift = getShiftByDate(memberId, LocalDate.now());
        shift.checkout(requestDto.checkoutTime());
        return shift;
    }

    @Transactional(readOnly = true)
    public Shift getShiftByDate(final String memberId, final LocalDate date) {
        final Member member = memberDao.findById(memberId);
        final LocalDateTime start = date.atStartOfDay();
        final LocalDateTime end = start.plusDays(1).minusNanos(1);
        Shift shift = shiftRepository.findShiftByMemberAndCheckin_CheckinTimeBetween(member, start, end)
                .orElseThrow(() -> new ShiftNotFoundException());
        return shift;
    }

    @Transactional(readOnly = true)
    public List<ShiftResponse> getShiftsInRange(String memberId, LocalDate startDate, LocalDate endDate) {
        final Member member = memberDao.findById(memberId);
        final LocalDateTime start = startDate.atStartOfDay();
        final LocalDateTime end = endDate.plusDays(1).atStartOfDay().minusNanos(1);
        final List<Shift> shifts = shiftRepository.findShiftsByMemberAndCheckin_CheckinTimeBetween(member, start, end);
        return shifts.stream().map(shift -> new ShiftResponse(shift)).collect(Collectors.toList());
    }

    private void checkDuplicateCheckin(final Member member) {
        final LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        final LocalDateTime end = start.plusDays(1).minusNanos(1);
        if (shiftRepository.existsByMemberAndCheckin_CheckinTimeBetween(member, start, end)) {
            throw new CheckinAlreadyExistsException();
        }
    }
}
