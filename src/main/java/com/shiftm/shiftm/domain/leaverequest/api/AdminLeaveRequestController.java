package com.shiftm.shiftm.domain.leaverequest.api;

import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.dto.request.LeaveRequestStatusRequest;
import com.shiftm.shiftm.domain.leaverequest.dto.response.AdminLeaveRequestResponse;
import com.shiftm.shiftm.domain.leaverequest.dto.response.LeaveRequestListResponse;
import com.shiftm.shiftm.domain.leaverequest.dto.response.LeaveRequestResponse;
import com.shiftm.shiftm.domain.leaverequest.service.LeaveRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/admin/leave-request")
@RestController
public class AdminLeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @GetMapping
    public LeaveRequestListResponse getAllLeaveRequests(@RequestParam(defaultValue = "0") final int page,
                                                        @RequestParam(defaultValue = "10") final int size) {
        final Pageable pageable = PageRequest.of(page, size);

        final Page<LeaveRequest> leaveRequests = leaveRequestService.getAllLeaveRequests(pageable);

        final List<LeaveRequestResponse> content = leaveRequests.getContent().stream()
                .map(LeaveRequestResponse::of)
                .toList();

        return new LeaveRequestListResponse(content, page, size, leaveRequests.getTotalPages(), leaveRequests.getTotalElements());
    }

    @GetMapping("/{memberId}")
    public LeaveRequestListResponse getLeaveRequest(@PathVariable("memberId") final String memberId,
                                                    @RequestParam(defaultValue = "0") final int page,
                                                    @RequestParam(defaultValue = "10") final int size) {
        final Pageable pageable = PageRequest.of(page, size);

        final Page<LeaveRequest> leaveRequests = leaveRequestService.getLeaveRequest(memberId, pageable);

        final List<LeaveRequestResponse> content = leaveRequests.getContent().stream()
                .map(LeaveRequestResponse::of)
                .toList();

        return new LeaveRequestListResponse(content, page, size, leaveRequests.getTotalPages(), leaveRequests.getTotalElements());
    }

    @PatchMapping("/{leaveRequestId}")
    public AdminLeaveRequestResponse updateLeaveRequest(@PathVariable("leaveRequestId") final Long leaveRequestId,
                                                        @Valid @RequestBody final LeaveRequestStatusRequest requestDto) {
        final LeaveRequest leaveRequest = leaveRequestService.updateLeaveRequestStatus(leaveRequestId, requestDto);

        return AdminLeaveRequestResponse.of(leaveRequest);
    }
}