package com.webflux.webflux_reactive_annotations.controller;

import java.time.Duration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/hello")
public class HelloController {

    @GetMapping("/mono")
    public Mono<String> getMono() {
        return Mono.just("¡Bienvenidos a todos!");
    }

    // Deprecated: .APPLICATION_STREAM_JSON_VALUE
    // Current: .APPLICATION_NDJSON_VALUE
    // @GetMapping(path = "/flux", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @GetMapping("/flux")
    public Flux<String> getFlux() {
        Flux<String> message = Flux.just("Bienvenido", "a", "mi", "API REST")
            .delayElements(Duration.ofSeconds(1))
            .log();

        return message;
    }
    
}
