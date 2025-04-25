package com.exceptions;

import reactor.core.publisher.Flux;

public class OnErrorContinue {
    
    public static void main(String[] args) {
        Flux.just(2, 0, 10, 8, 12, 22, 24)
            .map(element -> {
                if (element == 0) {
                    throw new RuntimeException("Ocurrio una excepcion");
                }
                return element;
            })
            .onErrorContinue((ex, element) -> {
                System.out.println("Excepción: " + ex);
                System.out.println("El elmento que causa la excepción es: " + element);
            })
            .log()
            .subscribe();
    }

}
