package com.backpressure;

import java.util.ArrayList;
import java.util.Arrays;

import reactor.core.publisher.Flux;

public class Example01 {

    public static void main(String[] args) {
        Flux<String> cities = Flux.fromIterable(
            new ArrayList<>(Arrays.asList(
                "New York", "Londo", "Paris",
                "Toronto", "Rome"
            ))
        );
        
        cities.log().subscribe();
    }

}