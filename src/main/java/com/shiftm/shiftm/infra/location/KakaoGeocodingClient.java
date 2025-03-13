package com.shiftm.shiftm.infra.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Component
public class KakaoGeocodingClient {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    private static final String GEOCODING_API_URL = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x={longitude}&y={latitude}";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public KakaoGeocodingClient(RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    public String getAddress(final double latitude, final double longitude) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        final HttpEntity<String> entity = new HttpEntity<>(headers);

        final String response = restTemplate.exchange(GEOCODING_API_URL, HttpMethod.GET, entity, String.class, longitude, latitude).getBody();
        try {
            final KakaoGeocodingResponse geocodingResponse = objectMapper.readValue(response, KakaoGeocodingResponse.class);
            if (geocodingResponse.documents() != null && !geocodingResponse.documents().isEmpty()) {
                return geocodingResponse.documents().get(0).address().addressName();
            }
        } catch (Exception e) {
            log.error("주소 변환 실패: ", e);
            return "주소 변환 실패";
        }
        return "주소를 찾을 수 없습니다";
    }

    public record KakaoGeocodingResponse(List<Document> documents) {
        public record Document(@JsonProperty("address") Address address) {}
        public record Address(@JsonProperty("address_name") String addressName) {}
    }
}
