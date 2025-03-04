package com.shiftm.shiftm.domain.leaverequest.api;

import com.shiftm.shiftm.domain.leaverequest.dto.request.RequestLeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.service.LeaveRequestService;
import com.shiftm.shiftm.global.auth.annotation.AuthId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/leave-request")
@RestController
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @PostMapping
    public void requestLeave(@AuthId final String memberId, @Valid @RequestBody final RequestLeaveRequest requestDto) {
        leaveRequestService.requestLeave(memberId, requestDto);
    }
}
