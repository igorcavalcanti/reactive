package com.example.reactive.service;

import com.example.reactive.domain.entities.Facility;
import com.example.reactive.repository.FacilityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.example.reactive.util.ReactiveUtil.fluxFromCollection;

@Service
@AllArgsConstructor
public class FacilityService {
    private final FacilityRepository repository;

    public Flux<Facility> findActiveFacilities(Long id) {
        if (id == null) {
            return fluxFromCollection(() -> repository.findByIsActive(true));
        } else {
            return fluxFromCollection(() -> repository.findByIdAndIsActive(id, true));
        }

    }

    public CompletableFuture<List<Facility>> findActiveFacilitiesFuture(Long id) {
        if (id == null) {
            return CompletableFuture.supplyAsync(() -> repository.findByIsActive(true));
        } else {
            return CompletableFuture.supplyAsync(() -> repository.findByIdAndIsActive(id, true));
        }

    }
}
