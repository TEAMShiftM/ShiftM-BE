package com.shiftm.shiftm.domain.leave.service;

import com.shiftm.shiftm.domain.leave.domain.LeaveType;
import com.shiftm.shiftm.domain.leave.dto.request.LeaveTypeRequest;
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
    public LeaveType createLeaveType(LeaveTypeRequest requestDto) {
        validateName(requestDto.name());
        return leaveTypeRepository.save(new LeaveType(requestDto.name()));
    }

    @Transactional
    public LeaveType updateLeaveType(Long leaveTypeId, LeaveTypeRequest requestDto) {
        LeaveType leaveType = findById(leaveTypeId);
        validateName(requestDto.name());

        leaveType.updateName(requestDto.name());
        return leaveType;
    }

    private void validateName(String name) {
        if (leaveTypeRepository.existsByName(name)) {
            throw new DuplicatedNameException(name);
        }
    }

    private LeaveType findById(Long id) {
        return leaveTypeRepository.findById(id).orElseThrow();
    }

}
