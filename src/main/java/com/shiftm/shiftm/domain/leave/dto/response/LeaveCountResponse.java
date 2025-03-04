package com.shiftm.shiftm.domain.leave.dto.response;

import java.util.List;

public record LeaveCountResponse(
        List<Long> infos,
        Double usableCount
) {
}