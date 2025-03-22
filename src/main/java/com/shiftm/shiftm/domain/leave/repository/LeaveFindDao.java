package com.shiftm.shiftm.domain.leave.repository;

import com.querydsl.core.Tuple;
import com.shiftm.shiftm.domain.leave.domain.Leave;
import com.shiftm.shiftm.domain.leave.domain.LeaveType;
import com.shiftm.shiftm.domain.leave.exception.LeaveNotFoundException;
import com.shiftm.shiftm.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class LeaveFindDao {

    private final LeaveRepository leaveRepository;
    private final CustomLeaveRepository customLeaveRepository;

    public Leave findById(final Long id) {
        return leaveRepository.findById(id).orElseThrow(() -> new LeaveNotFoundException(id));
    }

    public boolean existsValidLeaveForLeaveType(final Long leaveTypeId, final LocalDate date) {
        return customLeaveRepository.existsValidLeaveForLeaveType(leaveTypeId, date);
    }

    public Page<Leave> findLeaveByMember(final Member member, final Pageable pageable) {
        return customLeaveRepository.findLeaveByMember(member, pageable);
    }

    public Page<Leave> findAll(final Pageable pageable) {
        return customLeaveRepository.findAll(pageable);
    }

    public Tuple findByMemberIdAndLeaveTypeAndExpirationDate(final String memberId, final LeaveType leaveType,
                                                             final LocalDate date) {
        return customLeaveRepository.findByMemberIdAndLeaveTypeAndExpirationDate(memberId, leaveType, date);
    }
}