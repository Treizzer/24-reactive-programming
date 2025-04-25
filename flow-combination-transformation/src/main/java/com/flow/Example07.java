package com.flow;

import reactor.core.publisher.Flux;

public class Example07 {

    public static void main(String[] args) {
        Flux<Integer> flux1 = Flux.just(1, 2, 3, 4, 5);
        Flux<Integer> flux2 = Flux.just(4, 5, 6);

        flux1.zipWith(flux2, (f1, f2) -> f1 * f2)
            .log()
            .subscribe(System.out::println);
    }

}
