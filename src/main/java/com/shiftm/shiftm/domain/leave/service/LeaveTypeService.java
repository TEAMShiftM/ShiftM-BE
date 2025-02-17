package com.shiftm.shiftm.domain.leave.service;

import com.shiftm.shiftm.domain.leave.domain.LeaveType;
import com.shiftm.shiftm.domain.leave.dto.request.CreateLeaveTypeRequest;
import com.shiftm.shiftm.domain.leave.exception.DuplicatedNameException;
import com.shiftm.shiftm.domain.leave.repository.LeaveTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LeaveTypeService {

    private final LeaveTypeRepository leaveTypeRepository;

    @Transactional
    public LeaveType createLeaveType(CreateLeaveTypeRequest requestDto) {
        validateName(requestDto.name());
        return leaveTypeRepository.save(new LeaveType(requestDto.name()));
    }

    private void validateName(String name) {
        if (leaveTypeRepository.existsByName(name)) {
            throw new DuplicatedNameException(name);
        }
    }
}
