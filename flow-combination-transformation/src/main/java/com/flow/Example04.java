package com.flow;

import reactor.core.publisher.Flux;

public class Example04 {
    
    public static void main(String[] args) {
        Flux<String> flux1 = Flux.fromArray(new String[]{
            "a", "b", "c"
        });
        Flux<String> flux2 = Flux.fromArray(new String[] {
            "d", "e", "f"
        });

        Flux<String> fluxConcat = Flux.concat(flux1, flux2);

        fluxConcat.subscribe(element -> System.out.println(element + " "));
    }

}
