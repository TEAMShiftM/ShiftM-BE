package com.shiftm.shiftm.domain.shift.repository;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.shift.domain.Shift;
import com.shiftm.shiftm.domain.shift.exception.ShiftNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ShiftFindDao {

    private final ShiftRepository shiftRepository;

    public boolean existsByMemberAndCheckinTimeInRange(final Member member, final LocalDateTime start, final LocalDateTime end) {
        return shiftRepository.existsByMemberAndCheckinTimeInRange(member, start, end);
    }

    public Shift findById(final Long shiftId) {
        return shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ShiftNotFoundException());
    }

    public Shift findShiftByMemberAndCheckinTimeInRange(final Member member, final LocalDateTime start, final LocalDateTime end) {
        return shiftRepository.findShiftByMemberAndCheckinTimeInRange(member, start, end)
                .orElseThrow(() -> new ShiftNotFoundException());
    }

    public List<Shift> findShiftsByMemberAndCheckinTimeInRange(final Member member, final LocalDateTime start, final LocalDateTime end) {
            return shiftRepository.findShiftsByMemberAndCheckinTimeInRange(member, start, end);
    }
}
