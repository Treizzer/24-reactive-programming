package com.flow;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Example05 {
    
    public static void main(String[] args) {
        Flux<String> flux = Flux.fromArray(new String[]{
            "a", "b", "c"
        });
        Mono<String> mono = Mono.just("f");

        // Concat Flux and Mono publishers
        Flux<String> fluxConcat = flux.concatWith(mono);

        fluxConcat.subscribe(element -> System.out.println(element + " "));
    }

}
