package com.shiftm.shiftm.domain.leave.service;

import com.shiftm.shiftm.domain.leave.domain.LeaveType;
import com.shiftm.shiftm.domain.leave.dto.request.LeaveTypeRequest;
import com.shiftm.shiftm.domain.leave.exception.DuplicatedNameException;
import com.shiftm.shiftm.domain.leave.repository.LeaveTypeFindDao;
import com.shiftm.shiftm.domain.leave.repository.LeaveTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LeaveTypeService {

    private final LeaveTypeFindDao leaveTypeFindDao;
    private final LeaveTypeRepository leaveTypeRepository;

    @Transactional
    public LeaveType createLeaveType(final LeaveTypeRequest requestDto) {
        validateName(requestDto.name());

        final LeaveType leaveType = requestDto.toEntity(requestDto.name());
        return leaveTypeRepository.save(leaveType);
    }

    @Transactional
    public LeaveType updateLeaveType(final Long leaveTypeId, final LeaveTypeRequest requestDto) {
        final LeaveType leaveType = leaveTypeFindDao.findById(leaveTypeId);

        validateName(requestDto.name());

        leaveType.updateName(requestDto.name());
        return leaveType;
    }

    @Transactional(readOnly = true)
    public List<LeaveType> getAllLeaveType() {
        return leaveTypeFindDao.findByDeletedAtIsNull();
    }

    private void validateName(final String name) {
        if (leaveTypeRepository.existsByName(name)) {
            throw new DuplicatedNameException(name);
        }
    }
}
