package com.example.geo.shop_stream.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {
    // ObjectMapper is thread-safe after configuration — safe to share as a static field
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        try {
            return mapper.writeValueAsString(list);  // ["prod-A","prod-B"]
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not serialize productIds", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String json) {
        if (json == null || json.isBlank()) {
            return List.of();
        }
        try {
            return mapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not deserialize productIds: " + json, e);
        }
    }
}
