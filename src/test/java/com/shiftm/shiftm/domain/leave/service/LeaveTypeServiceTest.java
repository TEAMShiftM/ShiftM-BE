package com.shiftm.shiftm.domain.leave.service;

import com.shiftm.shiftm.domain.leave.domain.LeaveType;
import com.shiftm.shiftm.domain.leave.domain.LeaveTypeBuilder;
import com.shiftm.shiftm.domain.leave.dto.request.LeaveTypeRequest;
import com.shiftm.shiftm.domain.leave.exception.DuplicatedNameException;
import com.shiftm.shiftm.domain.leave.exception.LeaveExistsException;
import com.shiftm.shiftm.domain.leave.exception.LeaveTypeLockedException;
import com.shiftm.shiftm.domain.leave.exception.LeaveTypeNotFoundException;
import com.shiftm.shiftm.domain.leave.repository.*;
import com.shiftm.shiftm.test.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeaveTypeServiceTest extends UnitTest {

    private LeaveTypeService leaveTypeService;

    private LeaveTypeRepository leaveTypeRepository;

    private LeaveTypeFindDao leaveTypeFindDao;

    private LeaveFindDao leaveFindDao;

    private LeaveType leaveType;

    private LeaveType statutoryLeaveType;

    @BeforeEach
    public void setUp() {
        leaveFindDao = mock(LeaveFindDao.class);
        leaveTypeRepository = mock(LeaveTypeRepository.class);
        leaveTypeFindDao = new LeaveTypeFindDao(leaveTypeRepository);
        leaveTypeService = new LeaveTypeService(leaveFindDao, leaveTypeFindDao, leaveTypeRepository);

        statutoryLeaveType = LeaveTypeBuilder.build();
        leaveType = new LeaveType("경조사 휴가");
    }

    @DisplayName("연차 유형 생성 성공")
    @Test
    void 연차_유형_생성_성공() {
        // given
        final LeaveTypeRequest requestDto = new LeaveTypeRequest("경조사 휴가");

        when(leaveTypeRepository.existsByName(any())).thenReturn(false);
        when(leaveTypeRepository.save(any())).thenReturn(leaveType);

        // when, then
        assertEquals(leaveTypeService.createLeaveType(requestDto).getName(), "경조사 휴가");
    }

    @DisplayName("연차 유형 생성 실패 - 이름 중복")
    @Test
    public void 연차_유형_생성_실패_이름_중복() {
        // given
        final LeaveTypeRequest requestDto = new LeaveTypeRequest("경조사 휴가");

        when(leaveTypeRepository.existsByName(any())).thenReturn(true);

        // when, then
        assertThrows(DuplicatedNameException.class, () -> leaveTypeService.createLeaveType(requestDto));
    }

    @DisplayName("연차 유형 수정 성공")
    @Test
    public void 연차_유형_수정_성공() {
        // given
        final LeaveTypeRequest requestDto = new LeaveTypeRequest("경조사 휴가");

        when(leaveTypeRepository.findById(any())).thenReturn(Optional.of(leaveType));
        when(leaveTypeRepository.existsByName(any())).thenReturn(false);

        // when, then
        assertEquals(leaveTypeService.updateLeaveType(null, requestDto).getName(), "경조사 휴가");
    }

    @DisplayName("연차 유형 수정 실패 - 존재하지 않는 ID")
    @Test
    public void 연차유형_수정_실패_존재하지_않는_ID() {
        // given
        final LeaveTypeRequest requestDto = new LeaveTypeRequest("경조사 휴가");

        when(leaveTypeRepository.findById(any())).thenReturn(Optional.empty());

        // when, then
        assertThrows(LeaveTypeNotFoundException.class, () -> leaveTypeService.updateLeaveType(10L, requestDto));
    }

    @DisplayName("연차 유형 수정 실패 - 연차 유형 이름 중복")
    @Test
    public void 연차_유형_수정_실패_이름_중복() {
        // given
        final LeaveTypeRequest requestDto = new LeaveTypeRequest("경조사 휴가");

        when(leaveTypeRepository.findById(any())).thenReturn(Optional.of(leaveType));
        when(leaveTypeRepository.existsByName(any())).thenReturn(true);

        // when, then
        assertThrows(DuplicatedNameException.class, () -> leaveTypeService.updateLeaveType(null, requestDto));
    }

    @DisplayName("연차 유형 수정 실패 - 법정 연차")
    @Test
    public void 연차_유형_수정_실패_법정_연차() {
        // given
        final LeaveTypeRequest requestDto = new LeaveTypeRequest("경조사 휴가");

        when(leaveTypeRepository.findById(any())).thenReturn(Optional.of(statutoryLeaveType));

        // when, then
        assertThrows(LeaveTypeLockedException.class, () -> leaveTypeService.updateLeaveType(null, requestDto));
    }

    @DisplayName("연차 유형 수정 실패 - 삭제된 연차 유형")
    @Test
    public void 연차_유형_수정_실패_삭제된_연차_유형() {
        // given
        final LeaveTypeRequest requestDto = new LeaveTypeRequest("경조사 휴가");

        when(leaveTypeRepository.findById(any())).thenReturn(Optional.of(leaveType));
        leaveType.setDeletedAt(LocalDateTime.now());

        // when, then
        assertThrows(LeaveTypeNotFoundException.class, () -> leaveTypeService.updateLeaveType(null, requestDto));
    }

    @DisplayName("연차 유형 목록 조회")
    @Test
    public void 연차_유형_목록_조회() {
        // given
        final LeaveType deletedLeaveType = LeaveTypeBuilder.build();
        deletedLeaveType.setDeletedAt(LocalDateTime.now());

        final List<LeaveType> leaveTypeList = List.of(leaveType, leaveType, deletedLeaveType);

        when(leaveTypeFindDao.findByDeletedAtIsNull()).thenReturn(leaveTypeList.stream()
                .filter(leaveType -> leaveType.getDeletedAt() == null)
                .collect(Collectors.toList()));

        // when, then
        assertEquals(leaveTypeService.getAllLeaveType().size(), leaveTypeList.size() - 1);
    }

    @DisplayName("연차 유형 삭제 실패 - 법정 연차")
    @Test
    public void 연차_유형_삭제_실패_법정_연차() {
        // given
        when(leaveTypeRepository.findById(any())).thenReturn(Optional.of(statutoryLeaveType));

        // when, then
        assertThrows(LeaveTypeLockedException.class, () -> leaveTypeService.deleteLeaveType(null));
    }

    @DisplayName("연차 유형 삭제 실패 - 삭제된 연차 유형")
    @Test
    public void 연차_유형_삭제_실패_삭제된_연차_유형() {
        // given
        when(leaveTypeRepository.findById(any())).thenReturn(Optional.of(leaveType));
        leaveType.setDeletedAt(LocalDateTime.now());

        // when, then
        assertThrows(LeaveTypeNotFoundException.class, () -> leaveTypeService.deleteLeaveType(null));
    }

    @DisplayName("연차 유형 삭제 실패 - 연차 존재")
    @Test
    public void 연차_유형_삭제_실패_연차_존재() {
        // given
        when(leaveTypeRepository.findById(any())).thenReturn(Optional.of(leaveType));
        when(leaveFindDao.existsValidLeaveForLeaveType(any(), any())).thenReturn(true);

        // when, then
        assertThrows(LeaveExistsException.class, () -> leaveTypeService.deleteLeaveType(null));
    }
}