package com.example.reactive.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "\"FacilityRequirement\"")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class FacilityRequirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "facility_id", nullable = false, updatable = false)
    private Facility facility;

    @ManyToOne(optional = false)
    @JoinColumn(name = "document_id", nullable = false, updatable = false)
    private Document document;
}