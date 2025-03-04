package com.shiftm.shiftm.domain.leaverequest.dto.request;

import com.shiftm.shiftm.domain.leaverequest.domain.enums.Status;

public record UpdateLeaveRequestRequest(
        Status status
) {
}