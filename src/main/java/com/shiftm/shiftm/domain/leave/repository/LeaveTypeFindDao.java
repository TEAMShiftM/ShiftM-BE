package com.shiftm.shiftm.domain.leave.repository;

import com.shiftm.shiftm.domain.leave.domain.LeaveType;
import com.shiftm.shiftm.domain.leave.exception.LeaveTypeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LeaveTypeFindDao {

    private final LeaveTypeRepository leaveTypeRepository;

    public LeaveType findById(final Long id) {
        return leaveTypeRepository.findById(id).orElseThrow(() -> new LeaveTypeNotFoundException(id));
    }

    public LeaveType findValidById(final Long id) {
        final LeaveType leaveType = leaveTypeRepository.findById(id)
                .orElseThrow(() -> new LeaveTypeNotFoundException(id));

        if (leaveType.getDeletedAt() != null) {
            throw new LeaveTypeNotFoundException(id);
        }

        return leaveType;
    }

    public List<LeaveType> findByDeletedAtIsNull() {
        return leaveTypeRepository.findByDeletedAtIsNull();
    }
}