package com.reactive;

import java.time.Duration;
import java.util.stream.Stream;

import reactor.core.publisher.Flux;

// By default
public class ColdPublisher {

    public static void main(String[] args) throws InterruptedException {
        Flux<String> netFlux = Flux.fromStream(ColdPublisher::getVideo)
            .delayElements(Duration.ofSeconds(2));

        netFlux.subscribe(part -> System.out.println("Suscriptor 1: " + part));
        Thread.sleep(5000);

        netFlux.subscribe(part -> System.out.println("Suscriptor 2: " + part));
        Thread.sleep(6000);

    }

    private static Stream<String> getVideo() {
        System.out.println("Petición para el vídeo");

        return Stream.of("Pt. 1", "Pt. 2", "Pt. 3", "Pt. 4", "Pt. 5");
    }

}