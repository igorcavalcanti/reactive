package com.example.reactive.domain.projection;

import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

public interface ClaimedShiftsProjection {

    @Value("#{@timestampToZonedDateTimeConverter.convertToEntityAttribute(target.start)}")
    ZonedDateTime getStart();

    @Value("#{@timestampToZonedDateTimeConverter.convertToEntityAttribute(target.end)}")
    ZonedDateTime getEnd();

    public default Mono<Boolean> overlaps(ClaimedShiftsProjection shift) {
        return Mono.just((this.getStart().isAfter(shift.getStart()) && this.getStart().isBefore(shift.getEnd()))
                || (this.getEnd().isAfter(shift.getStart()) && this.getEnd().isBefore(shift.getEnd())));
    }
}
