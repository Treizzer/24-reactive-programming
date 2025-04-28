package com.webflux.webflux_reactive_annotations.webflux.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.webflux.webflux_reactive_annotations.webflux.handler.HelloHandler;

@Configuration
public class HelloRouter {

    @Bean
    public RouterFunction<ServerResponse> functionalRoutes(HelloHandler handler) {
        return RouterFunctions
            .route(RequestPredicates.GET("/functional/mono"), handler::showMonoMessage)
            .andRoute(RequestPredicates.GET("/functional/flux"), handler::showFluxMessage);
    }
    
}
