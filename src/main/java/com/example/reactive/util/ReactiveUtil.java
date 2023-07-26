package com.example.reactive.util;

import com.example.reactive.exception.InternalServerException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ReactiveUtil {
    public static <S> Flux<S> fluxFromCollection(Supplier<Collection<S>> supplier) {
        return Flux.create(emitter -> {
            CompletableFuture<Collection<S>> future = CompletableFuture.supplyAsync(supplier);
            future.whenCompleteAsync((result, ex) -> {
                if (ex != null) {
                    emitter.error(new InternalServerException(ex.getMessage(), ex));
                } else {
                    result.forEach(emitter::next);
                    emitter.complete();
                };
            });
        });
    }

    public static <T> Mono<T> monoFrom(Supplier<T> supplier) {
        return Mono.fromSupplier(supplier);
    }
}
