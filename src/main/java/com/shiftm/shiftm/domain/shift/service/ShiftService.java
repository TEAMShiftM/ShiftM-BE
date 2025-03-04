package com.shiftm.shiftm.domain.shift.service;

import com.shiftm.shiftm.domain.member.domain.Member;
import com.shiftm.shiftm.domain.member.repository.MemberDao;
import com.shiftm.shiftm.domain.shift.domain.Shift;
import com.shiftm.shiftm.domain.shift.domain.enums.Status;
import com.shiftm.shiftm.domain.shift.dto.request.*;
import com.shiftm.shiftm.domain.shift.exception.CheckinAlreadyExistsException;
import com.shiftm.shiftm.domain.shift.exception.ShiftNotFoundException;
import com.shiftm.shiftm.domain.shift.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ShiftService {

    private static final LocalTime PIVOT_TIME = LocalTime.of(4, 0);

    private final ShiftRepository shiftRepository;
    private final MemberDao memberDao;

    @Transactional
    public Shift createCheckin(final String memberId, final CheckinRequest requestDto) {
        final Member member = memberDao.findById(memberId);
        validateDuplicateCheckin(member);
        final Shift shift = requestDto.toEntity(member);
        return shiftRepository.save(shift);
    }

    @Transactional
    public Shift createAfterCheckin(final String memberId, final AfterCheckinRequest requestDto) {
        final Member member = memberDao.findById(memberId);
        validateDuplicateCheckin(member);
        final Shift shift = requestDto.toEntity(member);
        return shiftRepository.save(shift);
    }

    @Transactional
    public Shift createCheckout(final String memberId, final CheckoutRequest requestDto) {
        final Shift shift = getShiftForCurrentDay(memberId);
        shift.checkout(requestDto.checkoutTime());
        return shift;
    }

    @Transactional(readOnly = true)
    public List<Shift> getShiftsInRange(final String memberId, final LocalDate startDate, final LocalDate endDate) {
        final Member member = memberDao.findById(memberId);
        final LocalDateTime start = (startDate != null) ? startDate.atStartOfDay() : null;
        final LocalDateTime end = (endDate != null) ? endDate.plusDays(1).atStartOfDay().minusNanos(1) : null;
        return shiftRepository.findShiftsByMemberAndCheckinTimeInRange(member, start, end);
    }

    @Transactional(readOnly = true)
    public Shift getShiftForCurrentDay(final String memberId) {
        final Member member = memberDao.findById(memberId);
        final LocalDate today = LocalDate.now();
        final LocalDateTime startOfDay = today.atStartOfDay();
        final LocalDateTime pivot = today.atTime(PIVOT_TIME);

        final LocalDateTime start = (LocalDateTime.now().isBefore(pivot)) ? startOfDay.minusDays(1) : startOfDay;
        final LocalDateTime end = start.plusDays(1).minusNanos(1);
        final Shift shift = shiftRepository.findShiftByMemberAndCheckinTimeInRange(member, start, end)
                .orElseThrow(() -> new ShiftNotFoundException());
        return shift;
    }

    @Transactional(readOnly = true)
    public Page<Shift> getShifts(final Pageable pageable, final String name) {
        if (name == null || name.isEmpty()) {
            return shiftRepository.findAll(pageable);
        }
        return shiftRepository.findByName(pageable, name);
    }

    @Transactional(readOnly = true)
    public Page<Shift> getAfterCheckin(final Pageable pageable, final String name) {
        if (name == null || name.isEmpty()) {
            return shiftRepository.findByStatusExcludeOrdered(pageable, Status.AUTO_APPROVED);
        }
        return shiftRepository.findByStatusExcludeAndNameOrdered(pageable, Status.AUTO_APPROVED, name);
    }

    public Shift updateAfterCheckinStatus(final Long shiftId, final ShiftStatusRequest requestDto) {
        final Shift shift = findById(shiftId);
        shift.updateStatus(requestDto.status());
        return shift;
    }

    @Transactional
    public Shift updateShift(final Long shiftId, final ShiftRequest requestDto) {
        final Shift shift = findById(shiftId);
        shift.update(requestDto.checkinTime(), requestDto.latitude(),
                requestDto.longitude(), requestDto.status(), requestDto.checkoutTime());
        return shift;
    }

    public byte[] exportShiftToExcel(List<Shift> shifts) throws IOException {
        final Workbook workbook = new XSSFWorkbook();
        final Sheet sheet = workbook.createSheet("근무 기록");

        final Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("사원명");
        headerRow.createCell(1).setCellValue("출근 시간");
        headerRow.createCell(2).setCellValue("퇴근 시간");

        int rowNum = 1;
        for (Shift shift : shifts) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(shift.getMember().getName());
            row.createCell(1).setCellValue(shift.getCheckin().getCheckinTime().toString());
            row.createCell(2).setCellValue(shift.getCheckout().getCheckoutTime().toString());
        }

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();

        return byteArrayOutputStream.toByteArray();
    }

    private void validateDuplicateCheckin(final Member member) {
        final LocalDateTime start = LocalDate.now().atStartOfDay();
        final LocalDateTime end = start.plusDays(1).minusNanos(1);
        if (shiftRepository.existsByMemberAndCheckinTimeInRange(member, start, end)) {
            throw new CheckinAlreadyExistsException();
        }
    }

    private Shift findById(final Long shiftId) {
        return shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ShiftNotFoundException());
    }
}
