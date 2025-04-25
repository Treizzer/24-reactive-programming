package com.step.verifier.service_test_step_verifier.service;

import java.time.Duration;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SimpleService {

    public Mono<String> searchOne() {
        return Mono.just("Hola");
    }

    public Flux<String> searchAll() {
        return Flux.just("Hola", "que", "tal", "estas");
    }

    public Flux<String> searchAllSlow() {
        return Flux.just("Hola", "que", "tal", "estas").delaySequence(Duration.ofSeconds(10));
    }
    
}
