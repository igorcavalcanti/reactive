package com.example.reactive.service;

import com.example.reactive.domain.entities.Profession;
import com.example.reactive.repository.WorkerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

import static com.example.reactive.util.ReactiveUtil.monoFrom;

@Service
@AllArgsConstructor
public class WorkerService {

    private final WorkerRepository repository;

    public Mono<Profession> findProfessionById(Long workerId) {
        return monoFrom(() -> repository.findProfessionById(workerId));
    }

    public CompletableFuture<Profession> findProfessionByIdFuture(Long workerId) {
        return CompletableFuture.supplyAsync(() -> repository.findProfessionById(workerId));
    }
}
