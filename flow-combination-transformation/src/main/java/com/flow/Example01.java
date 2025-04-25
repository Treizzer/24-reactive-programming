package com.flow;

import reactor.core.publisher.Flux;

public class Example01 {

    public static void main(String[] args) {
        Flux.fromArray(new String[] {
            "Hugo", "Paco", "Luis", "Sandra"
        })
        .map(String::toUpperCase)
        .subscribe(System.out::println);
    }

}