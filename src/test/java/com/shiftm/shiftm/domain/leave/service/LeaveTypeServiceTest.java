package com.shiftm.shiftm.domain.leave.service;

import com.shiftm.shiftm.domain.leave.domain.LeaveType;
import com.shiftm.shiftm.domain.leave.domain.LeaveTypeBuilder;
import com.shiftm.shiftm.domain.leave.dto.request.LeaveTypeRequest;
import com.shiftm.shiftm.domain.leave.exception.DuplicatedNameException;
import com.shiftm.shiftm.domain.leave.exception.LeaveTypeNotFoundException;
import com.shiftm.shiftm.domain.leave.repository.LeaveTypeFindDao;
import com.shiftm.shiftm.domain.leave.repository.LeaveTypeRepository;
import com.shiftm.shiftm.test.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeaveTypeServiceTest extends UnitTest {

    private LeaveTypeService leaveTypeService;

    private LeaveTypeRepository leaveTypeRepository;

    private LeaveTypeFindDao leaveTypeFindDao;

    private LeaveType leaveType;

    @BeforeEach
    public void setUp() {
        leaveTypeRepository = mock(LeaveTypeRepository.class);
        leaveTypeFindDao = new LeaveTypeFindDao(leaveTypeRepository);
        leaveTypeService = new LeaveTypeService(leaveTypeFindDao, leaveTypeRepository);

        leaveType = LeaveTypeBuilder.build();
    }

    @DisplayName("연차 유형 생성 성공")
    @Test
    void 연차_유형_생성_성공() {
        // given
        final LeaveTypeRequest requestDto = new LeaveTypeRequest("연차유급휴가");

        when(leaveTypeRepository.existsByName(any())).thenReturn(false);
        when(leaveTypeRepository.save(any())).thenReturn(leaveType);

        // when, then
        assertEquals(leaveTypeService.createLeaveType(requestDto).getName(), "연차유급휴가");
    }

    @DisplayName("연차 유형 생성 실패 - 이름 중복")
    @Test
    public void 연차_유형_생성_실패_이름_중복() {
        // given
        final LeaveTypeRequest requestDto = new LeaveTypeRequest("연차유급휴가");

        when(leaveTypeRepository.existsByName(any())).thenReturn(true);

        // when, then
        assertThrows(DuplicatedNameException.class, () -> leaveTypeService.createLeaveType(requestDto));
    }

    @DisplayName("연차 유형 수정 성공")
    @Test
    public void 연차_유형_수정_성공() {
        // given
        final LeaveTypeRequest requestDto = new LeaveTypeRequest("생리휴가");

        when(leaveTypeRepository.findById(any())).thenReturn(Optional.of(leaveType));

        // when, then
        assertEquals(leaveTypeService.updateLeaveType(null, requestDto).getName(), "생리휴가");
    }

    @DisplayName("연차 유형 수정 실패 - 존재하지 않는 ID")
    @Test
    public void 연차유형_수정_실패_존재하지_않는_ID() {
        // given
        final LeaveTypeRequest requestDto = new LeaveTypeRequest("생리 휴가");

        when(leaveTypeRepository.findById(any())).thenReturn(Optional.empty());

        // when, then
        assertThrows(LeaveTypeNotFoundException.class, () -> leaveTypeService.updateLeaveType(10L, requestDto));
    }

    @DisplayName("연차 유형 수정 실패 - 연차 유형 이름 중복")
    @Test
    public void 연차_유형_수정_실패_이름_중복() {
        // given
        final LeaveTypeRequest requestDto = new LeaveTypeRequest("생리 휴가");

        when(leaveTypeRepository.findById(any())).thenReturn(Optional.of(leaveType));
        when(leaveTypeRepository.existsByName(any())).thenReturn(true);

        // when, then
        assertThrows(DuplicatedNameException.class, () -> leaveTypeService.updateLeaveType(null, requestDto));
    }

    @DisplayName("연차 유형 목록 조회")
    @Test
    public void 연차_유형_목록_조회() {
        // given
        final List<LeaveType> leaveTypeList = List.of(leaveType, leaveType, leaveType);

        when(leaveTypeFindDao.findByDeletedAtIsNull()).thenReturn(leaveTypeList);

        // when, then
        assertEquals(leaveTypeService.getAllLeaveType().size(), leaveTypeList.size());
    }
}