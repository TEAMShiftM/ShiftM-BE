package com.shiftm.shiftm.domain.leaverequest.api;

import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.dto.request.RequestLeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.dto.request.UpdateLeaveRequestRequest;
import com.shiftm.shiftm.domain.leaverequest.dto.response.LeaveRequestListResponse;
import com.shiftm.shiftm.domain.leaverequest.dto.response.LeaveRequestResponse;
import com.shiftm.shiftm.domain.leaverequest.service.LeaveRequestService;
import com.shiftm.shiftm.global.auth.annotation.AuthId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/leave-request")
@RestController
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @PostMapping
    public void requestLeave(@AuthId final String memberId, @Valid @RequestBody final RequestLeaveRequest requestDto) {
        leaveRequestService.requestLeave(memberId, requestDto);
    }

    @GetMapping
    public LeaveRequestListResponse getRequestLeaveInfos(@AuthId final String memberId, @RequestParam(defaultValue = "0") final int page,
                                                         @RequestParam(defaultValue = "10") final int size) {
        final Pageable pageable = PageRequest.of(page, size);

        final Page<LeaveRequest> leaveRequests = leaveRequestService.getRequestLeaveInfos(memberId, pageable);

        final List<LeaveRequestResponse> content = leaveRequests.getContent().stream()
                .map(LeaveRequestResponse::new)
                .toList();

        return new LeaveRequestListResponse(content, page, size, leaveRequests.getTotalPages(), leaveRequests.getTotalElements());
    }

    @PatchMapping("/{leaveRequestId}")
    public void updateLeaveRequest(@AuthId final String memberId, @PathVariable("leaveRequestId") final Long leaveRequestId,
                                   @Valid @RequestBody final UpdateLeaveRequestRequest requestDto) {
        leaveRequestService.updateLeaveRequest(memberId, leaveRequestId, requestDto);
    }
}
