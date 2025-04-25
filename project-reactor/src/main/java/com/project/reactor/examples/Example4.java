package com.project.reactor.examples;

import reactor.core.publisher.Flux;

public class Example4 {
    
    public static void main(String[] args) {
        Flux<String> flux = Flux.fromArray(new String[] {"Data1", "Data2", "Data3"});

        // flux.subscribe(System.out::println);
        flux.doOnNext(System.out::println).subscribe();
    }

}
