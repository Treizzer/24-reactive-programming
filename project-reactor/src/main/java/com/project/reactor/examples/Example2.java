// prettier-ignore
package com.project.reactor.examples;

import reactor.core.publisher.Mono;

public class Example2 {

    public static void main(String[] args) {
        Mono<String> mono = Mono.just("Hola");
        // mono.subscribe(System.out::println);

        mono.subscribe(
            // data -> System.out.println(data),
            System.out::println, // onNext
            // err -> System.out.println(err), // onError
            System.out::println, // onError
            () -> System.out.println("¡Completado!") // onComplete
        );
    }

}
