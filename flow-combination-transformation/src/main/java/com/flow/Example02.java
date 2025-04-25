package com.flow;

import reactor.core.publisher.Flux;

public class Example02 {
    
    public static void main(String[] args) {
        Flux.fromArray(new String[] {
            "Hugo", "Paco", "Luis", "Sandra"
        })
        .filter(name -> name.length() > 5)
        .map(String::toUpperCase) // Sync
        .subscribe(System.out::println);
    }

}
