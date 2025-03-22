package com.shiftm.shiftm.domain.leave.dto.response;

import com.querydsl.core.Tuple;

public record LeaveCountResponse(
        Long leaveId,
        Long leaveTypeId,
        String leaveTypeName,
        Double count,
        Double usableCount
) {
    public static LeaveCountResponse of(final Tuple leaveTuple) {
        if (leaveTuple == null) {
            return new LeaveCountResponse(null, null, null, null, null);
        }
        return new LeaveCountResponse(
                leaveTuple.get(0, Long.class),
                leaveTuple.get(1, Long.class),
                leaveTuple.get(2, String.class),
                leaveTuple.get(3, Double.class),
                leaveTuple.get(4, Double.class)
        );
    }
}