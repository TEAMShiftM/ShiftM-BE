package com.shiftm.shiftm.infra.file;

import com.shiftm.shiftm.domain.shift.domain.Shift;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class ExcelFileService {

    public void generateExcelFile(final HttpServletResponse response, final List<Shift> shifts) {
        final Workbook workbook = new XSSFWorkbook();
        try {
            final Sheet sheet = workbook.createSheet("Shifts");

            final Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("사원명");
            headerRow.createCell(1).setCellValue("출근 시간");
            headerRow.createCell(2).setCellValue("퇴근 시간");

            int rowNum = 1;
            for (final Shift shift : shifts) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(shift.getMember().getName());
                row.createCell(1).setCellValue(shift.getCheckin().getCheckinTime().toString());
                row.createCell(2).setCellValue(shift.getCheckout() != null ? shift.getCheckout().getCheckoutTime().toString() : "미제출");
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"shifts.xlsx\"");

            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            log.error("Error occurred while generating the Excel file", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("Error occurred while generating the file.");
            } catch (IOException innerException) {
                log.error("Error occurred while writing the error message", innerException);
            }
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                log.error("Error occurred while closing the workbook", e);
            }
        }
    }
}
