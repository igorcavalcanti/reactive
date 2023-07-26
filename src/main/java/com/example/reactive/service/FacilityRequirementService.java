package com.example.reactive.service;

import com.example.reactive.repository.FacilityRequirementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.example.reactive.util.ReactiveUtil.monoFrom;

@Service
@AllArgsConstructor
public class FacilityRequirementService {

    private final FacilityRequirementRepository repository;

    public Mono<List<Long>> findDocumentsIdByFacilityId(Long facilityId) {
        return monoFrom(() -> repository.findDocumentsIdByFacilityId(facilityId));
    }

    public CompletableFuture<List<Long>> findDocumentsIdByFacilityIdFuture(Long facilityId) {
        return CompletableFuture.supplyAsync(() -> repository.findDocumentsIdByFacilityId(facilityId));
    }
}
