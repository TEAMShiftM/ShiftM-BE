package com.shiftm.shiftm.domain.leave.repository;

import com.shiftm.shiftm.domain.leave.domain.Leave;
import com.shiftm.shiftm.domain.leave.exception.LeaveNotFoundException;
import lombok.RequiredArgsConstructor;
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
}