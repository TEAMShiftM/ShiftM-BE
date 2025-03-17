package com.shiftm.shiftm.infra.holiday;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.collect;

@Component
public class HolidayClient {

    @Value("${external.holiday.api.key}")
    private String serviceKey;

    private static final String HOLIDAY_API_URL = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo";

    private final RestTemplate restTemplate;

    public HolidayClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public List<LocalDate> getHolidays(final int year, final int month) {
        final String url = UriComponentsBuilder.fromHttpUrl(HOLIDAY_API_URL)
                .queryParam("solYear", year)
                .queryParam("solMonth", String.format("%02d", month))
                .queryParam("ServiceKey", serviceKey)
                .queryParam("_type", "json")
                .queryParam("numOfRows", 100)
                .toUriString();

        final ResponseEntity<HolidayResponse> response = restTemplate.getForEntity(url, HolidayResponse.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody()
                    .body()
                    .items()
                    .item()
                    .stream()
                    .map(item -> LocalDate.parse(String.valueOf(item.locdate())))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public List<LocalDate> getHolidaysBetweenDates(final LocalDate startDate, final LocalDate endDate) {
        final List<LocalDate> holidays = getHolidays(startDate.getYear(), startDate.getMonthValue());
        return holidays.stream()
                .filter(date -> !date.isBefore(startDate) && !date.isAfter(endDate))
                .collect(Collectors.toList());
    }
}