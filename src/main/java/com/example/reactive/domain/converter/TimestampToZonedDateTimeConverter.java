package com.example.reactive.domain.converter;

import jakarta.persistence.AttributeConverter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class TimestampToZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(ZonedDateTime attribute) {
        return Timestamp.from(attribute.toInstant());
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Timestamp dbData) {
        return ZonedDateTime.of(dbData.toLocalDateTime(), ZoneId.of("UTC"));
    }
}
