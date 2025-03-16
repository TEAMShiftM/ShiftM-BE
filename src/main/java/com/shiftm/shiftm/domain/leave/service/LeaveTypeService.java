package com.shiftm.shiftm.domain.leave.service;

import com.shiftm.shiftm.domain.leave.domain.LeaveType;
import com.shiftm.shiftm.domain.leave.dto.request.LeaveTypeRequest;
import com.shiftm.shiftm.domain.leave.exception.DuplicatedNameException;
import com.shiftm.shiftm.domain.leave.exception.LeaveTypeLockedException;
import com.shiftm.shiftm.domain.leave.exception.LeaveTypeNotFoundException;
import com.shiftm.shiftm.domain.leave.repository.LeaveTypeFindDao;
import com.shiftm.shiftm.domain.leave.repository.LeaveTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LeaveTypeService {

    private static final List<String> STATUTORY_LEAVE_TYPE = List.of("연차휴가", "출산휴가", "배우자출산휴가", "생리휴가", "가족돌봄휴가");

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

        validateActiveLeaveType(leaveType);

        validateStatutoryLeaveType(leaveType.getName());

        validateName(requestDto.name());

        leaveType.updateName(requestDto.name());
        return leaveType;
    }

    @Transactional(readOnly = true)
    public List<LeaveType> getAllLeaveType() {
        return leaveTypeFindDao.findByDeletedAtIsNull();
    }

    @Transactional
    public void deleteLeaveType(final Long leaveTypeId) {
        final LeaveType leaveType = leaveTypeFindDao.findById(leaveTypeId);

        validateStatutoryLeaveType(leaveType.getName());

        validateActiveLeaveType(leaveType);

        leaveType.setDeletedAt(LocalDateTime.now());
    }

    private void validateName(final String name) {
        if (leaveTypeRepository.existsByName(name)) {
            throw new DuplicatedNameException(name);
        }
    }

    private void validateStatutoryLeaveType(final String name) {
        if (STATUTORY_LEAVE_TYPE.contains(name)) {
            throw new LeaveTypeLockedException();
        }
    }

    private void validateActiveLeaveType(final LeaveType leaveType) {
        if (leaveType.getDeletedAt() != null) {
            throw new LeaveTypeNotFoundException(leaveType.getId());
        }
    }
}
