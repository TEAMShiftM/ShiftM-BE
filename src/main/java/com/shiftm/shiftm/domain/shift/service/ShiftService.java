package com.shiftm.shiftm.domain.shift.service;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.repository.MemberDao;
import com.shiftm.shiftm.domain.shift.domain.Shift;
import com.shiftm.shiftm.domain.shift.dto.request.AfterCheckinRequest;
import com.shiftm.shiftm.domain.shift.dto.request.CheckinRequest;
import com.shiftm.shiftm.domain.shift.dto.request.CheckoutRequest;
import com.shiftm.shiftm.domain.shift.exception.CheckinAlreadyExistsException;
import com.shiftm.shiftm.domain.shift.exception.ShiftNotFoundException;
import com.shiftm.shiftm.domain.shift.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final MemberDao memberDao;

    @Transactional
    public Shift createCheckin(final String memberId, final CheckinRequest requestDto) {
        final Member member = memberDao.findById(memberId);
        validateDuplicateCheckin(member);
        final Shift shift = requestDto.toEntity(member);
        return shiftRepository.save(shift);
    }

    @Transactional
    public Shift createAfterCheckin(final String memberId, final AfterCheckinRequest requestDto) {
        final Member member = memberDao.findById(memberId);
        validateDuplicateCheckin(member);
        final Shift shift = requestDto.toEntity(member);
        return shiftRepository.save(shift);
    }

    @Transactional
    public Shift createCheckout(final String memberId, final CheckoutRequest requestDto) {
        final Shift shift = getShiftForCurrentDay(memberId);
        shift.checkout(requestDto.checkoutTime());
        return shift;
    }

    @Transactional(readOnly = true)
    public List<Shift> getShiftsInRange(final String memberId, final LocalDate startDate, final LocalDate endDate) {
        final Member member = memberDao.findById(memberId);
        final LocalDateTime start = startDate.atStartOfDay();
        final LocalDateTime end = endDate.plusDays(1).atStartOfDay().minusNanos(1);
        return shiftRepository.findShiftsByMemberAndCheckin_CheckinTimeBetween(member, start, end);
    }

    @Transactional(readOnly = true)
    public Shift getShiftForCurrentDay(final String memberId) {
        final Member member = memberDao.findById(memberId);
        final LocalDate today = LocalDate.now();
        final LocalDateTime startOfDay = today.atStartOfDay(); // 오늘 자정
        final LocalDateTime pivot = today.atTime(4, 0); // 오늘 04:00

        final LocalDateTime start = (LocalDateTime.now().isBefore(pivot)) ? startOfDay.minusDays(1) : startOfDay;
        final LocalDateTime end = start.plusDays(1).minusNanos(1);
        final Shift shift = shiftRepository.findShiftByMemberAndCheckin_CheckinTimeBetween(member, start, end)
                .orElseThrow(() -> new ShiftNotFoundException());
        return shift;
    }

    private void validateDuplicateCheckin(final Member member) {
        final LocalDateTime start = LocalDate.now().atStartOfDay();
        final LocalDateTime end = start.plusDays(1).minusNanos(1);
        if (shiftRepository.existsByMemberAndCheckin_CheckinTimeBetween(member, start, end)) {
            throw new CheckinAlreadyExistsException();
        }
    }
}
