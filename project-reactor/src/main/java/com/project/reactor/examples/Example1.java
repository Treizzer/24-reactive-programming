package com.project.reactor.examples;

import java.util.ArrayList;
import java.util.List;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Example1 {

    public static void main(String[] args) {
        List<Integer> monoElements = new ArrayList<>();
        List<Integer> fluxElements = new ArrayList<>();

        // Creating a Mono: emits only 0 or 1 element
        Mono<Integer> mono = Mono.just(121);
        // Creating a Flux: emits 0 till N elements
        Flux<Integer> flux = Flux.just(12, 14, 9, 11, 12, 20, 23, 8, 5, 6, 7);

        // We subscribe to mono
        mono.subscribe(monoElements::add);
        // We subscribe to Flux
        flux.subscribe(fluxElements::add);

        System.out.println(monoElements);
        System.out.println(fluxElements);
    }

}