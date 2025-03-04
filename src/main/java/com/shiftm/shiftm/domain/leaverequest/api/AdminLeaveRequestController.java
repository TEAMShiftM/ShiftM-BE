package com.shiftm.shiftm.domain.leaverequest.api;

import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.dto.request.UpdateLeaveRequestRequest;
import com.shiftm.shiftm.domain.leaverequest.dto.response.LeaveRequestResponse;
import com.shiftm.shiftm.domain.leaverequest.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/admin/leave-request")
@RestController
public class AdminLeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @GetMapping
    public List<LeaveRequestResponse> getAllLeaveRequests() {
        final List<LeaveRequest> leaveRequests = leaveRequestService.getAllLeaveRequests();

        return leaveRequests.stream()
                .map(LeaveRequestResponse::new)
                .toList();
    }

    @GetMapping("/{memberId}")
    public List<LeaveRequestResponse> getLeaveRequest(@PathVariable("memberId") final String memberId) {
        final List<LeaveRequest> leaveRequests = leaveRequestService.getLeaveRequest(memberId);

        return leaveRequests.stream()
                .map(LeaveRequestResponse::new)
                .toList();
    }

    @PatchMapping("/{leaveRequestId}")
    public void updateLeaveRequest(@PathVariable("leaveRequestId") final Long leaveRequestId,
                                                         final UpdateLeaveRequestRequest requestDto) {
        leaveRequestService.updateLeaveRequestStatus(leaveRequestId, requestDto);
    }
}