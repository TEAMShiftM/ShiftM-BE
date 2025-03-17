package com.shiftm.shiftm.infra.holiday;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        final UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(HOLIDAY_API_URL)
                .queryParam("solYear", year)
                .queryParam("solMonth", String.format("%02d", month))
                .queryParam("ServiceKey", serviceKey)
                .queryParam("_type", "json")
                .queryParam("numOfRows", 100)
                .build(true);
        final String url = uriComponents.toUriString();
        final URI uri = URI.create(url);

        final ResponseEntity<HolidayResponse> response = restTemplate.getForEntity(uri, HolidayResponse.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody()
                    .response()
                    .body()
                    .items()
                    .item()
                    .stream()
                    .map(item -> LocalDate.parse(String.valueOf(item.locdate()), DateTimeFormatter.ofPattern("yyyyMMdd")))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public List<LocalDate> getHolidaysBetweenDates(final LocalDate startDate, final LocalDate endDate) {
        return IntStream.rangeClosed(startDate.getMonthValue(), endDate.getMonthValue()) // startDate와 endDate 사이의 모든 월을 처리
                .mapToObj(month -> getHolidays(startDate.getYear(), month)) // 각 월에 대해 공휴일 목록을 가져옴
                .flatMap(List::stream) // List<LocalDate> 리스트를 평탄화
                .filter(date -> !date.isBefore(startDate) && !date.isAfter(endDate)) // 날짜 범위 필터링
                .collect(Collectors.toList()); // 결과 리스트로 모음
    }
}