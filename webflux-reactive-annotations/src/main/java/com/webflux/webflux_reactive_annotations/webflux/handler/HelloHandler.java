package com.webflux.webflux_reactive_annotations.webflux.handler;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class HelloHandler {

    public Mono<ServerResponse> showMonoMessage(ServerRequest request) {
        return ServerResponse.ok()
            // .contentType(MediaType.TEXT_PLAIN)
            .contentType(MediaType.TEXT_HTML)
            .body(
                Mono.just("¡Bienvenido!"),
                String.class
            );
    }

    public Mono<ServerResponse> showFluxMessage(ServerRequest request) {
        return ServerResponse.ok()
            // .contentType(MediaType.APPLICATION_STREAM_JSON)
            .contentType(MediaType.TEXT_HTML)
            .body(
                Flux.just("Bienvenido", "a", "mi", "API RESt")
                    .delayElements(Duration.ofSeconds(1)),
                String.class
            );
    }
    
}
