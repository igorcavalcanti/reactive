package com.example.reactive.service;

import com.example.reactive.domain.dto.SearchWorkerShiftDTO;
import com.example.reactive.domain.entities.Facility;
import com.example.reactive.domain.entities.Profession;
import com.example.reactive.domain.projection.AvailableShiftsProjection;
import com.example.reactive.exception.InternalServerException;
import com.example.reactive.exception.NoSuchElementFoundException;
import com.example.reactive.repository.ShiftRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.ZonedDateTime;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.reactive.util.ReactiveUtil.monoFrom;

@Service
@AllArgsConstructor
@Slf4j
public class ShiftService {

    private final ShiftRepository repository;
    private final DocumentWorkerService documentWorkerService;
    private final FacilityService facilityService;
    private final FacilityRequirementService facilityRequirementService;
    private final WorkerService workerService;

    public CompletableFuture<List<SearchWorkerShiftDTO>> findAvailableShifts(Long workerId,
                                                                                    Long facilityId,
                                                                                    ZonedDateTime start,
                                                                                    ZonedDateTime end,
                                                                                    Pageable pageConfig) {
        CompletableFuture<Profession> profession = workerService.findProfessionByIdFuture(workerId);
        CompletableFuture<HashSet<Long>> workerDocuments = documentWorkerService.findDocumentFuture(workerId);
        CompletableFuture<Stream<Long>> availableFacilities = facilityService.findActiveFacilitiesFuture(facilityId)
                .thenApplyAsync(facilities -> facilities.parallelStream()
                        .map(facility -> new AbstractMap.SimpleEntry<>(facility.getId(), facilityRequirementService.findDocumentsIdByFacilityIdFuture(facility.getId())))
                        .filter(map -> workerDocuments.handleAsync((result, error) -> {
                            if (error == null) {
                                return result.containsAll(map.getValue().join());
                            } else {
                                return false;
                            }
                        }).join())
                        .map(AbstractMap.SimpleEntry::getKey));

        return profession.thenApply(professionResult -> {
            CompletableFuture<Stream<AvailableShiftsProjection>> availableShiftStream = availableFacilities
                    .thenApply(facilitiesResult ->  repository.findAvailableShiftsBy(workerId,
                            facilitiesResult.toList(),
                            professionResult.name(),
                            start, end, pageConfig))
                    .thenApply(Collection::parallelStream);

            return availableShiftStream
                    .thenApply(resultList -> resultList.
                            map(result -> SearchWorkerShiftDTO.builder()
                                    .facilityId(result.getFacilityId())
                                    .shifts(result.getShifts())
                                    .start(result.getStart())
                                    .end(result.getEnd())
                                    .build())
                            .collect(Collectors.toList()))
                    .join();
        });
    }

    public Mono<List<SearchWorkerShiftDTO>> findAvailableByWorkerAndFacility(Long workerId,
                                                                       Long facilityId,
                                                                       ZonedDateTime start,
                                                                       ZonedDateTime end,
                                                                       Pageable pageConfig) {
        Mono<Profession> profession = workerService.findProfessionById(workerId);
        Mono<List<Long>> workerDocuments = documentWorkerService.findDocumentIdByWorkerId(workerId)
                .collectList()
                .cache();

        Mono<List<Long>> availableFacilities = facilityService.findActiveFacilities(facilityId)
                .map(Facility::getId)
                .map(facilityIdFlux -> Tuples.of(facilityIdFlux, facilityRequirementService.findDocumentsIdByFacilityId(facilityIdFlux)))
                .filterWhen(tuple -> workerDocuments.zipWith(tuple.getT2())
                        .filter(documentsTuple -> new HashSet<>(documentsTuple.getT1()).containsAll(documentsTuple.getT2()))
                        .hasElement())
                .map(Tuple2::getT1)
                .collectList();

        Mono<List<AvailableShiftsProjection>> available = profession
                .map(professionValue ->  availableFacilities
                        .flatMap(availableFacility -> getAvailableShiftsBy(workerId,
                                        start,
                                        end,
                                        professionValue,
                                        availableFacility,
                                        pageConfig)))
                .flatMap(Function.identity());

        return available
                .flatMapMany(Flux::fromIterable)
                .map(result -> SearchWorkerShiftDTO.builder()
                        .facilityId(result.getFacilityId())
                        .shifts(result.getShifts())
                        .start(result.getStart())
                        .end(result.getEnd())
                        .build())
                .collectList()
                .onErrorMap(err -> new InternalServerException(err.getMessage(), err))
                .switchIfEmpty(Mono.error(new NoSuchElementFoundException("No Shift was found.")));
    }

    private Mono<List<AvailableShiftsProjection>> getAvailableShiftsBy(Long workerId,
                                                                 ZonedDateTime start,
                                                                 ZonedDateTime end,
                                                                 Profession value,
                                                                 List<Long> availableFacility,
                                                                 Pageable pageConfig) {
        return monoFrom(() ->
                repository.findAvailableShiftsBy(workerId, availableFacility, value.name(), start, end, pageConfig));
    }
}
