package com.example.reactive.service;

import com.example.reactive.repository.DocumentWorkerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

import static com.example.reactive.util.ReactiveUtil.fluxFromCollection;

@Service
@AllArgsConstructor
public class DocumentWorkerService {
    private final DocumentWorkerRepository repository;
    public Flux<Long> findDocumentIdByWorkerId(Long workerId) {
        return fluxFromCollection(() -> repository.findDocumentIdByWorkerId(workerId));
    }

    public CompletableFuture<HashSet<Long>> findDocumentFuture(Long workerId) {
        return CompletableFuture.supplyAsync(() -> new HashSet<>(repository.findDocumentIdByWorkerId(workerId)));
    }
}
