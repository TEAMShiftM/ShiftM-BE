package com.shiftm.shiftm.domain.leaverequest.service;

import com.shiftm.shiftm.domain.leave.repository.LeaveRepository;
import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequestBuilder;
import com.shiftm.shiftm.domain.leaverequest.domain.enums.Status;
import com.shiftm.shiftm.domain.leaverequest.dto.LeaveRequestStatusRequestBuilder;
import com.shiftm.shiftm.domain.leaverequest.dto.request.LeaveRequestStatusRequest;
import com.shiftm.shiftm.domain.leaverequest.exception.StatusAlreadyExistsException;
import com.shiftm.shiftm.domain.leaverequest.repository.LeaveRequestFindDao;
import com.shiftm.shiftm.domain.leaverequest.repository.LeaveRequestRepository;
import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.repository.MemberFindDao;
import com.shiftm.shiftm.test.UnitTest;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LeaveRequestServiceTest extends UnitTest {

    @InjectMocks
    private LeaveRequestService leaveRequestService;

    @Mock
    private MemberFindDao memberFindDao;

    @Mock
    private LeaveRequestFindDao leaveRequestFindDao;

    @Mock
    private LeaveRepository leaveRepository;

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @BeforeEach
    void setUp() {
    }

    @Description("연차 요청 상태 변경 성공")
    @Test
    void 연차_요청_상태_변경_성공() {
        // given
        final LeaveRequestStatusRequest requestDto = LeaveRequestStatusRequestBuilder.build(Status.APPROVED);
        final LeaveRequest leaveRequest = LeaveRequestBuilder.build(Status.PENDING);

        when(leaveRequestFindDao.findById(any())).thenReturn(leaveRequest);

        // when
        final LeaveRequest updatedLeaveRequest = leaveRequestService.updateLeaveRequestStatus(1L, requestDto);

        // then
        assertThat(leaveRequest.getStatus()).isEqualTo(updatedLeaveRequest.getStatus());
    }

    @Description("연차 요청 상태 변경 실패 - 이미 확정(승인 또는 거절)된 상태")
    @Test
    void 연차_요청_상태_변경_실패() {
        // given
        final LeaveRequestStatusRequest requestDto = LeaveRequestStatusRequestBuilder.build(Status.APPROVED);
        final LeaveRequest leaveRequest = LeaveRequestBuilder.build(Status.REJECTED);

        when(leaveRequestFindDao.findById(any())).thenReturn(leaveRequest);

        // when, then
        assertThrows(StatusAlreadyExistsException.class, () ->
                leaveRequestService.updateLeaveRequestStatus(1L, requestDto));
    }

    @Description("연차 요청 취소 성공")
    @Test
    void 연차_요청_취소_성공() {
        // given
        final Member member = Member.builder().build();
        final LeaveRequestStatusRequest requestDto = LeaveRequestStatusRequestBuilder.build(Status.CANCELED);
        final LeaveRequest leaveRequest = LeaveRequestBuilder.build(Status.PENDING);

        leaveRequest.updateMember(member);

        when(memberFindDao.findById(any())).thenReturn(member);
        when(leaveRequestFindDao.findById(any())).thenReturn(leaveRequest);

        // when
        final LeaveRequest updateLeaveRequest = leaveRequestService.cancelLeaveRequest("shiftM", 1L, requestDto);

        // then
        assertThat(updateLeaveRequest.getStatus()).isEqualTo(Status.CANCELED);
    }

    @Description("연차 요청 취소 실패 - 이미 확정(승인, 거절, 취소)된 상태")
    @Test
    void 연차_요청_취소_실패() {
        // given
        final Member member = Member.builder().build();
        final LeaveRequestStatusRequest requestDto = LeaveRequestStatusRequestBuilder.build(Status.PENDING);
        final LeaveRequest leaveRequest = LeaveRequestBuilder.build(Status.CANCELED);

        leaveRequest.updateMember(member);

        when(memberFindDao.findById(any())).thenReturn(member);
        when(leaveRequestFindDao.findById(any())).thenReturn(leaveRequest);

        // when, then
        assertThrows(StatusAlreadyExistsException.class, () ->
                leaveRequestService.cancelLeaveRequest("shiftM", 1L, requestDto));
    }
}
