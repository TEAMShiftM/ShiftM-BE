package com.shiftm.shiftm.infra.geocoding;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GeocodingResponse(List<Document> documents) {
    public record Document(@JsonProperty("address") Address address) {}
    public record Address(@JsonProperty("address_name") String addressName) {}
}

