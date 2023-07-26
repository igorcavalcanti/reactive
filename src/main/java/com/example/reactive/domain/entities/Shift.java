package com.example.reactive.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

@Entity
@Table(name = "\"Shift\"")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private ZonedDateTime start;

    @Column
    private ZonedDateTime end;

    @Enumerated(EnumType.STRING)
    private Profession profession;

    @ManyToOne(optional = false)
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public Mono<Boolean> overlaps(Shift shift) {
        return Mono.just((this.start.isAfter(shift.start) && this.start.isBefore(shift.end))
                || (this.end.isAfter(shift.start) && this.end.isBefore(shift.end)));
    }
}
