package com.shiftm.shiftm.infra.geocoding;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GeocodingService {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    private static final String GEOCODING_API_URL = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x={longitude}&y={latitude}";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GeocodingService(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    public String getAddress(final double latitude, final double longitude) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        final HttpEntity<String> entity = new HttpEntity<>(headers);

        final String response = restTemplate.exchange(GEOCODING_API_URL, HttpMethod.GET, entity, String.class, longitude, latitude).getBody();
        try {
            final GeocodingResponse geocodingResponse = objectMapper.readValue(response, GeocodingResponse.class);
            if (geocodingResponse.documents() != null && !geocodingResponse.documents().isEmpty()) {
                final String address = geocodingResponse.documents().get(0).address().addressName();
                return address;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "주소 변환 실패";
        }
        return "주소를 찾을 수 없습니다";
    }
}
