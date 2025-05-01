package com.reactive.reactive_mongodb.functional.handler;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.reactive.reactive_mongodb.presentation.dto.ContactDto;
import com.reactive.reactive_mongodb.presentation.dto.ContactInsertDto;
import com.reactive.reactive_mongodb.presentation.dto.ContactUpdateDto;
import com.reactive.reactive_mongodb.service.interfaces.ICommonService;

import reactor.core.publisher.Mono;

@Component
public class ContactHandler {
    
    @Autowired
    private ICommonService<ContactDto, ContactInsertDto, ContactUpdateDto> service;

    private Mono<ServerResponse> notFoundResponse() {
        return ServerResponse.notFound().build();
    }

    private Mono<ServerResponse> badRequestResponse() {
        return ServerResponse.badRequest().build();
    }

    private <T> Mono<ServerResponse> okResponse(T body) {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            // .contentType(MediaType.TEXT_HTML)
            .body(BodyInserters.fromValue(body));
    }

    // Find all
    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok()
            // .contentType(MediaType.TEXT_HTML)
            .contentType(MediaType.APPLICATION_JSON)
            .body(service.findAll(), ContactDto.class);
    }

    // Find by ID
    public Mono<ServerResponse> findById(ServerRequest request) {
        return Mono.justOrEmpty(request.pathVariable("id"))
        .filter(id -> !id.isBlank())
        .flatMap(id -> service.findById(id)
            .flatMap(this::okResponse) // Return 200 with the DTO
            .switchIfEmpty(notFoundResponse()) // Handle case where entity is not found 404
        ) // Call service method reactively
        .switchIfEmpty(badRequestResponse()); // Return 400 if ID is missing or blank
    }

    // Find by email
    public Mono<ServerResponse> findByEmail(ServerRequest request) {
        return Mono.justOrEmpty(request.pathVariable("email"))
            .filter(email -> !email.isBlank())
            .flatMap(email -> service.findByEmail(email)
                .flatMap(this::okResponse)
                .switchIfEmpty(notFoundResponse())
            )
            .switchIfEmpty(badRequestResponse());
    }

    // Save
    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(ContactInsertDto.class)
            .flatMap(service::save)
            .flatMap(dto -> 
                ServerResponse.created(
                    URI.create("/api/v1/contacts/" + dto.getId())
                )
                // .contentType(MediaType.TEXT_HTML)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dto))
            );
    }

    // Update
    public Mono<ServerResponse> update(ServerRequest request) {
        Mono<ContactUpdateDto> updatedDto = request.bodyToMono(ContactUpdateDto.class);
        
        return updatedDto.flatMap(contact -> {
            String id = request.pathVariables().containsKey("id") 
                ? request.pathVariable("id")
                : contact.getId();
            
            return service.update(contact, id);
        })
            .flatMap(this::okResponse)
            .switchIfEmpty(notFoundResponse())
            .onErrorResume(e ->
                ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        );
    }

    // Delete by Id
    public Mono<ServerResponse> deleteById(ServerRequest request) {
        return Mono.justOrEmpty(request.pathVariable("id"))
            .filter(id -> !id.isBlank())
            .flatMap(id -> service.deleteById(id)
                .flatMap(this::okResponse)
                .switchIfEmpty(notFoundResponse())
            )
            .switchIfEmpty(badRequestResponse());
    }
    
}
