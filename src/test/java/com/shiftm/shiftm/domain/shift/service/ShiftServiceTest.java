package com.shiftm.shiftm.domain.shift.service;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.repository.MemberFindDao;
import com.shiftm.shiftm.domain.shift.domain.Checkin;
import com.shiftm.shiftm.domain.shift.domain.Checkout;
import com.shiftm.shiftm.domain.shift.domain.Shift;
import com.shiftm.shiftm.domain.shift.domain.enums.Status;
import com.shiftm.shiftm.domain.shift.dto.request.AfterCheckinRequest;
import com.shiftm.shiftm.domain.shift.dto.request.CheckinRequest;
import com.shiftm.shiftm.domain.shift.dto.request.CheckoutRequest;
import com.shiftm.shiftm.domain.shift.exception.CheckinAlreadyExistsException;
import com.shiftm.shiftm.domain.shift.exception.ShiftNotFoundException;
import com.shiftm.shiftm.domain.shift.repository.ShiftRepository;
import com.shiftm.shiftm.test.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShiftServiceTest extends UnitTest {

    @InjectMocks
    private ShiftService shiftService;

    @Mock
    private ShiftRepository shiftRepository;

    @Mock
    private MemberFindDao memberFindDao;

    private LocalDateTime now;
    private Member member;
    private Shift shift;
    private Checkin checkin;
    private Checkout checkout;

    @BeforeEach
    public void setUp() throws Exception {
        now = LocalDateTime.now();
        member = Member.builder().build();
        checkin = Checkin.builder()
                .checkinTime(now)
                .latitude(0.0)
                .longitude(0.0)
                .status(Status.AUTO_APPROVED)
                .build();
        checkout = Checkout.builder()
                .checkoutTime(now)
                .build();
        shift = Shift.builder()
                .member(member)
                .checkin(checkin)
                .checkout(checkout)
                .build();
    }

    @DisplayName("출근 신청 성공")
    @Test
    public void 출근_신청_성공() {
        // given
        final CheckinRequest requestDto = new CheckinRequest(now);

        when(memberFindDao.findById(any())).thenReturn(member);
        when(shiftRepository.existsByMemberAndCheckinTimeInRange(any(), any(), any())).thenReturn(false);
        when(shiftRepository.save(any())).thenReturn(shift);

        // when
        final Shift createCheckin = shiftService.createCheckin(member.getId(), requestDto);

        // then
        assertThat(createCheckin).isNotNull();
        assertThat(createCheckin.getMember()).isEqualTo(member);
        assertThat(createCheckin.getCheckin()).isNotNull();
        assertThat(createCheckin.getCheckin().getCheckinTime()).isEqualTo(now);
    }

    @DisplayName("출근 신청 실패 - 이미 존재")
    @Test
    public void 출근_신청_실패_이미_존재() {
        // given
        final CheckinRequest requestDto = new CheckinRequest(now);

        when(shiftRepository.existsByMemberAndCheckinTimeInRange(any(), any(), any())).thenReturn(true);

        // when, then
        assertThrows(CheckinAlreadyExistsException.class, () -> shiftService.createCheckin(member.getId(), requestDto));
    }

    @DisplayName("사후 출근 신청 성공")
    @Test
    public void 사후_출근_신청_성공() {
        // given
        final AfterCheckinRequest requestDto = new AfterCheckinRequest(now, 0.0, 0.0);

        when(memberFindDao.findById(any())).thenReturn(member);
        when(shiftRepository.existsByMemberAndCheckinTimeInRange(any(), any(), any())).thenReturn(false);
        when(shiftRepository.save(any())).thenReturn(shift);

        // when
        final Shift createAfterCheckin = shiftService.createAfterCheckin(member.getId(), requestDto);

        // then
        assertThat(createAfterCheckin).isNotNull();
        assertThat(createAfterCheckin.getMember()).isEqualTo(member);
        assertThat(createAfterCheckin.getCheckin()).isNotNull();
        assertThat(createAfterCheckin.getCheckin().getCheckinTime()).isEqualTo(now);
        assertThat(createAfterCheckin.getCheckin().getLatitude()).isEqualTo(0.0);
        assertThat(createAfterCheckin.getCheckin().getLongitude()).isEqualTo(0.0);
    }

    @DisplayName("사후 출근 신청 실패 - 이미 존재")
    @Test
    public void 사후_출근_신청_실패_이미_존재() {
        // given
        final AfterCheckinRequest requestDto = new AfterCheckinRequest(now, 0.0, 0.0);

        when(shiftRepository.existsByMemberAndCheckinTimeInRange(any(), any(), any())).thenReturn(true);

        // when, then
        assertThrows(CheckinAlreadyExistsException.class, () -> shiftService.createAfterCheckin(member.getId(), requestDto));
    }

    @DisplayName("퇴근 신청 성공")
    @Test
    public void 퇴근_신청_성공() {
        // given
        final CheckoutRequest requestDto = new CheckoutRequest(now);

        when(memberFindDao.findById(any())).thenReturn(member);
        when(shiftRepository.findShiftByMemberAndCheckinTimeInRange(any(), any(), any())).thenReturn(Optional.of(shift));

        // when
        final Shift createCheckout = shiftService.createCheckout(member.getId(), requestDto);

        // then
        assertThat(createCheckout.getCheckout().getCheckoutTime()).isEqualTo(now);
    }

    @DisplayName("퇴근 신청 실패 - 출근 기록 없음")
    @Test
    public void 퇴근_신청_실패_출근_기록_없음() {
        // given
        final CheckoutRequest requestDto = new CheckoutRequest(now);

        when(shiftRepository.findShiftByMemberAndCheckinTimeInRange(any(), any(), any())).thenReturn(Optional.empty());

        // when, then
        assertThrows(ShiftNotFoundException.class, () -> shiftService.createCheckout(member.getId(), requestDto));
    }

    @DisplayName("근무 기록 기간별 조회 성공")
    @Test
    public void 근무_기록_기간별_조회_성공() {
        // given
        final List<Shift> shifts = List.of(shift, shift);

        when(memberFindDao.findById(any())).thenReturn(member);
        when(shiftRepository.findShiftsByMemberAndCheckinTimeInRange(any(), any(), any())).thenReturn(shifts);

        // when
        final List<Shift> getShifts = shiftService.getShiftsInRange(member.getId(), now.minusDays(2).toLocalDate(), now.toLocalDate());

        // then
        assertThat(getShifts).hasSize(2);
    }

    @DisplayName("근무 기록 기간별 조회 성공 - start가 null인 경우")
    @Test
    public void 근무_기록_기간별_조회_성공_start_null() {
        // given
        final List<Shift> shifts = List.of(shift, shift);

        when(memberFindDao.findById(any())).thenReturn(member);
        when(shiftRepository.findShiftsByMemberAndCheckinTimeInRange(any(), eq(null), any())).thenReturn(shifts);

        // when
        final List<Shift> getShifts = shiftService.getShiftsInRange(member.getId(), null, now.toLocalDate());

        // then
        assertThat(getShifts).hasSize(2);
    }

    @DisplayName("근무 기록 기간별 조회 성공 - end가 null인 경우")
    @Test
    public void 근무_기록_기간별_조회_성공_end_null() {
        // given
        final List<Shift> shifts = List.of(shift, shift);

        when(memberFindDao.findById(any())).thenReturn(member);
        when(shiftRepository.findShiftsByMemberAndCheckinTimeInRange(any(), any(), eq(null))).thenReturn(shifts);

        // when
        final List<Shift> getShifts = shiftService.getShiftsInRange(member.getId(), now.minusDays(2).toLocalDate(), null);

        // then
        assertThat(getShifts).hasSize(2);
    }

    @DisplayName("근무 기록 기간별 조회 성공 - start와 end가 null인 경우")
    @Test
    public void 근무_기록_기간별_조회_성공_start_end_null() {
        // given
        final List<Shift> shifts = List.of(shift, shift);

        when(memberFindDao.findById(any())).thenReturn(member);
        when(shiftRepository.findShiftsByMemberAndCheckinTimeInRange(any(), eq(null), eq(null))).thenReturn(shifts);

        // when
        final List<Shift> getShifts = shiftService.getShiftsInRange(member.getId(), null, null);

        // then
        assertThat(getShifts).hasSize(2);
    }
}