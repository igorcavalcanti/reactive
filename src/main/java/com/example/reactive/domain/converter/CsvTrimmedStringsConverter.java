package com.example.reactive.domain.converter;

import jakarta.persistence.AttributeConverter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class CsvTrimmedStringsConverter implements AttributeConverter<String[], String> {
    @Override
    public String convertToDatabaseColumn(String[] attribute) {
        return attribute == null ? null : Arrays.stream(attribute)
                .map(String::trim)
                .collect(Collectors.joining(","));
    }

    @Override
    public String[] convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Arrays.stream(dbData.split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }
}
