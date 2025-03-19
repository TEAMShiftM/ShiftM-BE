package com.shiftm.shiftm.domain.leave.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shiftm.shiftm.domain.leave.domain.QLeave;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@RequiredArgsConstructor
@Repository
public class CustomLeaveRepository {

    private final JPAQueryFactory queryFactory;
    private final QLeave qLeave = QLeave.leave;

    public boolean existsValidLeaveForLeaveType(final Long leaveTypeId, final LocalDate date) {
        return queryFactory.selectOne()
                .from(qLeave)
                .where(
                        qLeave.leaveType.id.eq(leaveTypeId),
                        qLeave.expirationDate.goe(date),
                        qLeave.count.gt(qLeave.usedCount)
                )
                .fetchFirst() != null;
    }
}