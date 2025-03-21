package com.shiftm.shiftm.domain.leave.service;

import com.shiftm.shiftm.domain.leave.domain.Leave;
import com.shiftm.shiftm.domain.leave.domain.LeaveBuilder;
import com.shiftm.shiftm.domain.leave.domain.LeaveType;
import com.shiftm.shiftm.domain.leave.domain.LeaveTypeBuilder;
import com.shiftm.shiftm.domain.leave.dto.CreateLeaveRequestBuilder;
import com.shiftm.shiftm.domain.leave.dto.UpdateLeaveRequestBuilder;
import com.shiftm.shiftm.domain.leave.dto.request.CreateLeaveRequest;
import com.shiftm.shiftm.domain.leave.dto.request.UpdateLeaveRequest;
import com.shiftm.shiftm.domain.leave.repository.LeaveFindDao;
import com.shiftm.shiftm.domain.leave.repository.LeaveRepository;
import com.shiftm.shiftm.domain.leave.repository.LeaveTypeFindDao;
import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.domain.MemberBuilder;
import com.shiftm.shiftm.domain.member.repository.MemberFindDao;
import com.shiftm.shiftm.test.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

class LeaveServiceTest extends UnitTest {

    @Mock
    private LeaveFindDao leaveFindDao;

    @Mock
    private MemberFindDao memberFindDao;

    @Mock
    private LeaveTypeFindDao leaveTypeFindDao;

    @Mock
    private LeaveRepository leaveRepository;

    @InjectMocks
    private LeaveService leaveService;

    private Leave leave;

    private Member member;

    private LeaveType leaveType;

    private List<Member> memberList;

    private List<Leave> leaveList;

    @BeforeEach
    void setUp() {
        leave = LeaveBuilder.build();
        leaveType = LeaveTypeBuilder.build();
        member = MemberBuilder.build();

        leaveList = new ArrayList<>();

        memberList = new ArrayList<>();
        Member member1 = Member.builder().id("shiftm1").build();
        Member member2 = Member.builder().id("shiftm2").build();
        Member member3 = Member.builder().id("shiftm3").build();
        memberList.add(member1);
        memberList.add(member2);
        memberList.add(member3);
    }

    @DisplayName("연차 생성 성공")
    @Test
    void 연차_생성_성공() {
        // given
        final CreateLeaveRequest requestDto = CreateLeaveRequestBuilder.build();

        leave.updateLeaveType(leaveType);
        leave.updateMember(memberList.get(0));
        leaveList.add(leave);
        leave.updateMember(memberList.get(1));
        leaveList.add(leave);
        leave.updateMember(memberList.get(2));
        leaveList.add(leave);

        when(memberFindDao.findByIdIn(anyList())).thenReturn(memberList);
        when(leaveTypeFindDao.findById(any())).thenReturn(leaveType);
        when(leaveRepository.saveAll(anyList())).thenReturn(leaveList);

        // when
        final List<Leave> savedleaveList = leaveService.createLeaves(requestDto);

        assertThat(savedleaveList.size()).isEqualTo(leaveList.size());
    }

    @DisplayName("연차 수정 성공")
    @Test
    void 연차_수정_성공() {
        // given
        final UpdateLeaveRequest requestDto = UpdateLeaveRequestBuilder.build();

        when(leaveFindDao.findById(any())).thenReturn(leave);
        when(leaveTypeFindDao.findById(any())).thenReturn(leaveType);
        leave.updateLeaveType(leaveType);

        // when
        final Leave leave = leaveService.updateLeave(null, requestDto);

        // then
        assertThat(leave.getLeaveType()).isEqualTo(leaveType);
    }
}