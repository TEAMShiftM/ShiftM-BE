package com.shiftm.shiftm.domain.leave.domain;

public class LeaveTypeBuilder {

    public static LeaveType build() {
        final String name = "연차휴가";

        return LeaveType.builder()
                .name(name)
                .build();
    }
}
