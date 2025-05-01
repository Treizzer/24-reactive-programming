package com.reactive.reactive_mongodb.functional.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
// import org.springframework.web.reactive.function.server.RequestPredicates;

import com.reactive.reactive_mongodb.functional.handler.ContactHandler;

@Configuration
public class ContactRouter {

    @Bean
    public RouterFunction<ServerResponse> route(ContactHandler handler) {
        return RouterFunctions.route()
        .GET("/functional/contacts", handler::findAll)
        .GET("/functional/contacts/{id}", handler::findById)
        .GET("/functional/contacts/email/{email}", handler::findByEmail)
        .POST("/functional/contacts", handler::save)
        .PUT("/functional/contacts/{id}", handler::update)
        .PUT("/functional/contacts", handler::update)
        .DELETE("/functional/contacts/{id}", handler::deleteById)
        .build();
    }
    
}
