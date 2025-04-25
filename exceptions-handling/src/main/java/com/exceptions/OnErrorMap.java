package com.exceptions;

import reactor.core.publisher.Flux;

public class OnErrorMap {
    
    public static void main(String[] args) {
        Flux.just(2, 0, 10, 8, 12, 22, 24)
            .map(element -> {
                if (element == 8) {
                    throw new RuntimeException("Ocurrio una excepcion");
                }
                return element;
            })
            .onErrorMap(ex -> {
                System.out.println("Excepción: " + ex);
                return new CustomException(ex.getMessage(), ex);
            })
            .log()
            .subscribe();
    }

    public static class CustomException extends Exception {
        public CustomException(String message, Throwable exception) {
            super(message, exception);
        }
    }
    
}
