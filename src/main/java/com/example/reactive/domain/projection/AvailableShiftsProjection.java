package com.example.reactive.domain.projection;

import org.springframework.beans.factory.annotation.Value;

import java.time.ZonedDateTime;

public interface AvailableShiftsProjection {
    Long getFacilityId();

    @Value("#{@csvTrimmedStringsConverter.convertToEntityAttribute(target.shifts)}")
    String[] getShifts();

    @Value("#{@timestampToZonedDateTimeConverter.convertToEntityAttribute(target.start)}")
    ZonedDateTime getStart();

    @Value("#{@timestampToZonedDateTimeConverter.convertToEntityAttribute(target.end)}")
    ZonedDateTime getEnd();
}
