package com.shiftm.shiftm.domain.leave.service;

import com.shiftm.shiftm.domain.leave.domain.Leave;
import com.shiftm.shiftm.domain.leave.domain.LeaveType;
import com.shiftm.shiftm.domain.leave.dto.request.CreateLeaveRequest;
import com.shiftm.shiftm.domain.leave.dto.request.UpdateLeaveRequest;
import com.shiftm.shiftm.domain.leave.repository.LeaveDao;
import com.shiftm.shiftm.domain.leave.repository.LeaveRepository;
import com.shiftm.shiftm.domain.leave.repository.LeaveTypeDao;
import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.exception.MemberNotFoundException;
import com.shiftm.shiftm.domain.member.repository.MemberDao;
import com.shiftm.shiftm.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LeaveService {

    private final MemberRepository memberRepository;
    private final MemberDao memberDao;
    private final LeaveTypeDao leaveTypeDao;
    private final LeaveDao leaveDao;
    private final LeaveRepository leaveRepository;

    @Transactional
    public void createLeaves(final CreateLeaveRequest requestDto) {
        final List<Member> members = memberRepository.findByIdIn(requestDto.memberIds());

        validateMembers(requestDto.memberIds(), members);

        final LeaveType leaveType = leaveTypeDao.findById(requestDto.leaveTypeId());

        final List<Leave> leaves = requestDto.memberIds().stream()
                .map(memberId -> createLeave(memberId, toEntity(requestDto, leaveType)))
                .toList();

        leaveRepository.saveAll(leaves);
    }

    @Transactional
    public void updateLeave(final Long leaveId, final UpdateLeaveRequest requestDto) {
        final Leave leave = leaveDao.findById(leaveId);

        if (leave.getLeaveType().getId() != requestDto.leaveTypeId()) {
            final LeaveType leaveType = leaveTypeDao.findById(requestDto.leaveTypeId());

            leave.updateLeaveType(leaveType);
        }

        leave.updateLeave(requestDto.count(), requestDto.expirationDate());
    }

    private Leave toEntity(final CreateLeaveRequest requestDto, final LeaveType leaveType) {
        final Leave leave = requestDto.toEntity();

        leave.updateLeaveType(leaveType);

        return leave;
    }

    private Leave createLeave(final String memberId, final Leave leave) {
        final Member member = memberDao.findById(memberId);

        leave.updateMember(member);

        return leave;
    }

    private void validateMembers(final List<String> memberIds, final List<Member> members) {
        if (memberIds.size() != members.size()) {
            final List<String> validIds = members.stream()
                    .map(Member::getId)
                    .toList();

            memberIds.stream()
                    .filter(id -> !validIds.contains(id))
                    .findAny()
                    .ifPresent(id -> {
                        throw new MemberNotFoundException(id);
                    });
        }
    }
}
