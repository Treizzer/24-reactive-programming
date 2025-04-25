package com.flow;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Example03 {

    public static void main(String[] args) {
        // Flatmap can return several outputs (Async)
        // From 1 element to N elements
        Flux.fromArray(new String[]{
            "Hugo", "Paco", "Luis", "Sandra"
        })
        .flatMap(Example03::putNameModifiedInMono)
        .subscribe(System.out::println);
    }

    private static Mono<String> putNameModifiedInMono(String name) {
        return Mono.just(name.concat(" modificado"));
    }
    
}
