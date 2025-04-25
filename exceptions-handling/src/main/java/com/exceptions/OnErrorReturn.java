package com.exceptions;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class OnErrorReturn {

    public static void main(String[] args) {
        Flux.just(2, 7, 10)
            .concatWith(Flux.error(new RuntimeException("Ocurrio una excepcion")))
            .concatWith(Mono.just(12))
            .onErrorReturn(72)
            .log()
            .subscribe();
    }

}