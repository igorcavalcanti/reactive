package com.example.reactive.controller;

import com.example.reactive.domain.dto.SearchWorkerShiftDTO;
import com.example.reactive.service.ShiftService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/v1/shift")
@AllArgsConstructor
public class ShiftController {
    private final ShiftService service;

//    @Operation(summary = "Gets all available Shifts",
//            description = "Gets all available Shifts whose are inside of the period defined with the query params \"start\" and \"end\"." +
//                    "A shift is available for a worker only if it's not claimed by someone else and when both have the same Profession type. " +
//                    "Besides, every Shift has a Facility which a worker must fulfill its requirements to be considered in the result.",
//            parameters = {@Parameter(name = "workerId", description = "The worker's identification."),
//                    @Parameter(name = "facilityId", description = "The Facility's identification"),
//                    @Parameter(name = "start", description = "The lower bound of the desired period."),
//                    @Parameter(name = "end", description = "The upper bound of the desired period."),
//                    @Parameter(name = "page",
//                            description = "Zero-based page index (0..N)",
//                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0"))),
//                    @Parameter(name = "size",
//                            description = "The size of the page to be returned",
//                            content = @Content(schema = @Schema(type = "integer", defaultValue = "30")))
//
//            })
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "400", description = "Invalid range bounds informed"),
//            @ApiResponse(responseCode = "404", description = "Worker not found or inactive or Facility not found.")
//    })
//    @GetMapping("/available/worker/{workerId}")
//    public Flux<SearchWorkerShiftDTO> findAvailableByWorkerAndFacility(
//            @PathVariable("workerId") Long workerId,
//            @RequestParam(value = "facilityId", required = false) Long facilityId,
//            @RequestParam("start") ZonedDateTime start,
//            @RequestParam("end") ZonedDateTime end,
//            Pageable pageConfig) {
//        return service.findAvailableByWorkerAndFacility(workerId, facilityId, start, end, pageConfig);
//    }

    @GetMapping("/available/worker/{workerId}")
    public CompletableFuture<List<SearchWorkerShiftDTO>> findAvailableByWorkerAndFacilityCompletable(
            @PathVariable("workerId") Long workerId,
            @RequestParam(value = "facilityId", required = false) Long facilityId,
            @RequestParam("start") ZonedDateTime start,
            @RequestParam("end") ZonedDateTime end,
            Pageable pageConfig) {
        return service.findAvailableShifts(workerId, facilityId, start, end, pageConfig);
    }

    @GetMapping("/available/reactive/worker/{workerId}")
    public Mono<List<SearchWorkerShiftDTO>> findAvailableByWorkerAndFacilityMono(
            @PathVariable("workerId") Long workerId,
            @RequestParam(value = "facilityId", required = false) Long facilityId,
            @RequestParam("start") ZonedDateTime start,
            @RequestParam("end") ZonedDateTime end,
            Pageable pageConfig) {
        return service.findAvailableByWorkerAndFacility(workerId, facilityId, start, end, pageConfig);
    }
}
