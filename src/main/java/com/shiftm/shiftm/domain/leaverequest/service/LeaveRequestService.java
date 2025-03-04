package com.shiftm.shiftm.domain.leaverequest.service;

import com.shiftm.shiftm.domain.leave.domain.Leave;
import com.shiftm.shiftm.domain.leave.repository.LeaveRepository;
import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.domain.enums.Status;
import com.shiftm.shiftm.domain.leaverequest.dto.request.RequestLeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.dto.request.UpdateLeaveRequestRequest;
import com.shiftm.shiftm.domain.leaverequest.exception.LeaveNotEnoughException;
import com.shiftm.shiftm.domain.leaverequest.exception.LeaveRequestNotAuthorException;
import com.shiftm.shiftm.domain.leaverequest.exception.LeaveRequestUpdateFailedException;
import com.shiftm.shiftm.domain.leaverequest.repository.LeaveRequestRepository;
import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.repository.MemberDao;
import com.shiftm.shiftm.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LeaveRequestService {

    private final MemberDao memberDao;
    private final LeaveRepository leaveRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    @Transactional
    public void requestLeave(final String memberId, final RequestLeaveRequest requestDto) {
        final Member member = memberDao.findById(memberId);

        final List<Leave> leaves = leaveRepository.findLeaves(memberId, requestDto.leaveTypeId(), LocalDate.now());

        final Double usableCount = leaves.stream()
                .mapToDouble(leave -> leave.getCount() - leave.getUsedCount())
                .sum();

        if (requestDto.count() > usableCount) {
            throw new LeaveNotEnoughException(ErrorCode.LEAVE_NOT_ENOUGH);
        }

        final List<LeaveRequest> leaveRequests = new ArrayList<>();

        Double count = requestDto.count();

        for (Leave leave : leaves) {
            if (requestDto.count() <= 0) {
                break;
            }

            double available = leave.getCount() - leave.getUsedCount();

            LeaveRequest leaveRequest;

            if (available >= count) {
                leaveRequest = new LeaveRequest(requestDto.startDate(), requestDto.endDate(), count, Status.PENDING);
                leaveRequest.updateLeave(leave);
                leaveRequest.updateMember(member);
                leaveRequests.add(leaveRequest);
                break;
            } else {
                leaveRequest = new LeaveRequest(requestDto.startDate(), requestDto.endDate(), available, Status.PENDING);
                leaveRequest.updateLeave(leave);
                leaveRequest.updateMember(member);
                leaveRequests.add(leaveRequest);
                count -= available;
            }
        }

        leaveRequestRepository.saveAll(leaveRequests);
    }

    @Transactional(readOnly = true)
    public List<LeaveRequest> getRequestLeaveInfos(final String memberId) {
        final Member member = memberDao.findById(memberId);

        return leaveRequestRepository.findByMember(member);
    }

    @Transactional
    public void updateLeaveRequest(final String memberId, final Long leaveRequestId, final UpdateLeaveRequestRequest request) {
        final Member member = memberDao.findById(memberId);

        final LeaveRequest leaveRequest = findById(leaveRequestId);

        if (member != leaveRequest.getMember()) {
            throw new LeaveRequestNotAuthorException(ErrorCode.LEAVE_REQUEST_NOT_AUTHOR);
        }

        if (leaveRequest.getStatus() != Status.PENDING || leaveRequest.getStatus() != Status.CANCELED) {
            throw new LeaveRequestUpdateFailedException(ErrorCode.LEAVE_REQUEST_UPDATE_FAILED);
        }

        leaveRequest.updateStatus(request.status());
    }

    @Transactional(readOnly = true)
    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<LeaveRequest> getLeaveRequest(final String memberId) {
        final Member member = memberDao.findById(memberId);

        return leaveRequestRepository.findByMember(member);
    }

    private LeaveRequest findById(final Long leaveRequestId) {
        return leaveRequestRepository.findById(leaveRequestId).orElse(null);
    }
}
