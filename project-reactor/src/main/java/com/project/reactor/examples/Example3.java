package com.project.reactor.examples;

import reactor.core.publisher.Mono;

public class Example3 {

    public static void main(String[] args) {
        Mono<String> mono = Mono.fromSupplier(() -> {
            throw new RuntimeException("Excepcion Ocurrida");
        });

        mono.subscribe(
            System.out::println,
            System.out::println,
            () -> System.out.println("¡Completado!")
        );
    }
    
}
