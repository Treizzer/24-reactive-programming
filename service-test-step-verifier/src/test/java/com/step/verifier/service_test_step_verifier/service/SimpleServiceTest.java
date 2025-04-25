package com.step.verifier.service_test_step_verifier.service;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class SimpleServiceTest {
    
    // Given
    private SimpleService service;

    @BeforeEach
    public void init() {
        this.service = new SimpleService();
    }

    @Test
    public void testMono() {
        // When
        Mono<String> one = this.service.searchOne();
        
        // Then
        StepVerifier.create(one)
            .expectNext("Hola")
            // then
            .verifyComplete();
    }

    @Test
    public void testSearchAll() {
        // When
        Flux<String> several = this.service.searchAll();

        // Then
        StepVerifier.create(several)
            .expectNext("Hola")
            .expectNext("que")
            .expectNext("tal")
            .expectNext("estas")
            .verifyComplete();
    }

    @Test
    public void testSearchAllSlow() {
        // When
        Flux<String> several = this.service.searchAllSlow();

        // Then
        StepVerifier.create(several)
            .expectNext("Hola")
            .thenAwait(Duration.ofSeconds(1))
            .expectNext("que")
            .thenAwait(Duration.ofSeconds(1))
            .expectNext("tal")
            .thenAwait(Duration.ofSeconds(1))
            .expectNext("estas")
            .thenAwait(Duration.ofSeconds(1))
            .verifyComplete();
    }

}
