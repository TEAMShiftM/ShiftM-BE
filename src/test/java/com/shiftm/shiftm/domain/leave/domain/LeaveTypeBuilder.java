package com.shiftm.shiftm.domain.leave.domain;

public class LeaveTypeBuilder {

    public static LeaveType build() {
        final String name = "연차유급휴가";

        return LeaveType.builder()
                .name(name)
                .build();
    }
}
