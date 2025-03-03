package com.shiftm.shiftm.domain.shift.service;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.repository.MemberDao;
import com.shiftm.shiftm.domain.shift.domain.Shift;
import com.shiftm.shiftm.domain.shift.dto.request.CheckinRequest;
import com.shiftm.shiftm.domain.shift.exception.DuplicatedCheckinException;
import com.shiftm.shiftm.domain.shift.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private void checkDuplicateCheckin(final Member member) {
        LocalDateTime start = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = start.plusDays(1).minusNanos(1);
        if (shiftRepository.existsByMemberAndCheckin_CheckinTimeBetween(member, start, end)) {
            throw new DuplicatedCheckinException(LocalDate.now().toString());
        }
    }
}
