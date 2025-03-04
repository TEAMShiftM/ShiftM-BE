package com.shiftm.shiftm.domain.leave.repository;

import com.shiftm.shiftm.domain.leave.domain.LeaveType;
import com.shiftm.shiftm.domain.leave.exception.LeaveNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LeaveTypeDao {

    private final LeaveTypeRepository leaveTypeRepository;

    public LeaveType findById(final Long id) {
        return leaveTypeRepository.findById(id).orElseThrow(() -> new LeaveNotFoundException(id));
    }
}