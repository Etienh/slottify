package com.slottify.appointment_scheduler.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Converter
public class JsonMapConverter implements AttributeConverter<Map<String, String>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, String> attribute) {
        try {
            return attribute == null ? null : objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting Map to JSON", e);
        }
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String dbData) {
        try {
            return dbData == null ? null : objectMapper.readValue(dbData, HashMap.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading JSON into Map", e);
        }
    }
}
