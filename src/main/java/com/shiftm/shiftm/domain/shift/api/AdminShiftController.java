package com.shiftm.shiftm.domain.shift.api;

import com.shiftm.shiftm.domain.shift.domain.Shift;
import com.shiftm.shiftm.domain.shift.dto.request.ShiftRequest;
import com.shiftm.shiftm.domain.shift.dto.request.ShiftStatusRequest;
import com.shiftm.shiftm.domain.shift.dto.response.AdminShiftListResponse;
import com.shiftm.shiftm.domain.shift.dto.response.AdminShiftResponse;
import com.shiftm.shiftm.domain.shift.dto.response.AfterCheckinListResponse;
import com.shiftm.shiftm.domain.shift.dto.response.AfterCheckinResponse;
import com.shiftm.shiftm.domain.shift.repository.ShiftRepository;
import com.shiftm.shiftm.domain.shift.service.ShiftService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/admin/shift")
@RestController
public class AdminShiftController {

    private final ShiftService shiftService;
    private final ShiftRepository shiftRespository;

    // 전체 근무 기록 조회
    @GetMapping
    public AdminShiftListResponse getShifts(@PageableDefault Pageable pageable,
                                            @RequestParam(required = false) final String name) {
        Page<Shift> shiftPage = shiftService.getShifts(pageable, name);
        List<AdminShiftResponse> shifts = shiftPage.getContent().stream()
                .map(shift -> new AdminShiftResponse(shift))
                .collect(Collectors.toList());
        return new AdminShiftListResponse(shifts, shiftPage.getNumber(), shiftPage.getSize(), shiftPage.getTotalPages(), shiftPage.getTotalElements());
    }

    // 사후 출근 신청 조회
    @GetMapping("/after-checkin")
    public AfterCheckinListResponse getAfterCheckin(@PageableDefault Pageable pageable,
                                                    @RequestParam(required = false) final String name) {
        Page<Shift> shiftPage = shiftService.getAfterCheckin(pageable, name);
        final List<AfterCheckinResponse> shifts = shiftPage.getContent().stream()
                .map(shift -> new AfterCheckinResponse(shift))
                .collect(Collectors.toList());
        return new AfterCheckinListResponse(shifts, shiftPage.getNumber(), shiftPage.getSize(), shiftPage.getTotalPages(), shiftPage.getTotalElements());
    }

    // 출근 상태 변경
    @PatchMapping("/{shiftId}/status")
    public AfterCheckinResponse updateAfterCheckinStatus(@PathVariable final Long shiftId, @Valid @RequestBody final ShiftStatusRequest requestDto) {
        return new AfterCheckinResponse(shiftService.updateAfterCheckinStatus(shiftId, requestDto));
    }

    // 근무 기록 수정
    @PatchMapping("/{shiftId}")
    public AdminShiftResponse updateShift(@PathVariable final Long shiftId, @Valid @RequestBody final ShiftRequest requestDto) {
        return new AdminShiftResponse(shiftService.updateShift(shiftId, requestDto));
    }

    // 근무 기록 삭제
    @DeleteMapping("/{shiftId}")
    public void deleteShift(@PathVariable final Long shiftId) {
        shiftRespository.deleteById(shiftId);
    }

    // 엑셀 파일 변환
    @GetMapping("/export")
    public void exportShiftRecords(HttpServletResponse response) throws IOException {
        final List<Shift> shifts = shiftRespository.findAll();
        final byte[] excelData = shiftService.exportShiftToExcel(shifts);

        final String encodedFileName = URLEncoder.encode("근무기록.xlsx", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename=" + encodedFileName);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        response.getOutputStream().write(excelData);
        response.flushBuffer();
    }
}
