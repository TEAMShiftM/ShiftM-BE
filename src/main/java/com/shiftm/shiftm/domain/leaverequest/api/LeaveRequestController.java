package com.shiftm.shiftm.domain.leaverequest.api;

import com.shiftm.shiftm.domain.leaverequest.domain.LeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.dto.request.RequestLeaveRequest;
import com.shiftm.shiftm.domain.leaverequest.dto.response.LeaveRequestResponse;
import com.shiftm.shiftm.domain.leaverequest.service.LeaveRequestService;
import com.shiftm.shiftm.global.auth.annotation.AuthId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public List<LeaveRequestResponse> getRequestLeaveInfos(@AuthId final String memberId) {
        List<LeaveRequest> requests = leaveRequestService.getRequestLeaveInfos(memberId);

        return requests.stream()
                .map(request -> new LeaveRequestResponse(request))
                .toList();
    }
}
