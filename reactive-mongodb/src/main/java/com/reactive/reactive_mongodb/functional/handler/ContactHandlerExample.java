package com.reactive.reactive_mongodb.functional.handler;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.reactive.reactive_mongodb.persistence.entity.ContactEntity;
import com.reactive.reactive_mongodb.persistence.repository.IContactRepository;
import com.reactive.reactive_mongodb.presentation.dto.ContactDto;
import com.reactive.reactive_mongodb.presentation.dto.ContactInsertDto;
import com.reactive.reactive_mongodb.presentation.dto.ContactUpdateDto;

import reactor.core.publisher.Mono;

// @Component
public class ContactHandlerExample {

    @Autowired
    private IContactRepository repository;

    private Mono<ServerResponse> response404 = ServerResponse.notFound().build();

    private Mono<ServerResponse> response400 = ServerResponse.badRequest().build();

    // List contacts
    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok()
            .contentType(MediaType.TEXT_HTML)
            .body(repository.findAll(), ContactEntity.class);
    }

    // Find by id
    public Mono<ServerResponse> findById(ServerRequest request) {
        String id = request.pathVariable("id");
        if (id.isBlank()) {
            return response400;
            // return ServerResponse.badRequest()
            //     .contentType(MediaType.TEXT_HTML)
            //     .build();
        }

        return repository.findById(id)
            .flatMap(contact -> 
                ServerResponse.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(BodyInserters.fromValue(contact))
            )
            .switchIfEmpty(response404);
    }

    // Find by email
    public Mono<ServerResponse> findByEamil(ServerRequest request) {
        String email = request.pathVariable("email");

        if (email.isBlank()) {
            // return ServerResponse.badRequest()
            //     .contentType(MediaType.TEXT_HTML)
            //     .build();
            return response400;
        }

        return repository.findByEmail(email)
            .flatMap(contact ->
                ServerResponse.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(BodyInserters.fromValue(contact)) 
            )
            .switchIfEmpty(response404);
    }

    // Save a contact
    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<ContactInsertDto> monoContact = request.bodyToMono(ContactInsertDto.class);

        return monoContact.flatMap(contact ->
            repository.save(
                new ContactEntity(contact.getName(), contact.getEmail(), contact.getPhone())
            )
            .flatMap(savedContact ->
                ServerResponse.created(
                    URI.create("/api/v1/contacts/" + savedContact.getId())
                )
                .contentType(MediaType.TEXT_HTML)
                .body(BodyInserters.fromValue(savedContact))
            )
        );
    }

    // Update a contact
    public Mono<ServerResponse> update(ServerRequest request) {
        Mono<ContactUpdateDto> updatedDto = request.bodyToMono(ContactUpdateDto.class);
        String id = request.pathVariable("id");
        // if (id.isEmpty()) {}

        Mono<ContactDto> dto = updatedDto.flatMap(current ->
            repository.findById(id)
                .flatMap(old -> {
                    old.setName(current.getName());
                    old.setEmail(current.getEmail());
                    old.setPhone(current.getPhone());

                    return repository.save(old)
                        .map(newer ->
                            new ContactDto(newer.getId(), newer.getName(), newer.getEmail(), newer.getPhone()) 
                        );
                })
        );

        return dto.flatMap(contact -> 
            ServerResponse.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(BodyInserters.fromValue(contact))
        )
        .switchIfEmpty(response404);
    }

    // Delete by ID
    public Mono<ServerResponse> deleteById(ServerRequest request) {
        String id = request.pathVariable("id");
        if (id.isBlank()) {
            return response400;
        }

        Mono<ContactDto> dto = repository.findById(id)
            .flatMap(contact -> 
            repository.deleteById(id)
                .thenReturn(
                    new ContactDto(contact.getId(), contact.getName(), contact.getEmail(), contact.getPhone())
                )
            );

        return dto.flatMap(c ->
            ServerResponse.ok()
            .contentType(MediaType.TEXT_HTML)
            .body(BodyInserters.fromValue(c))
        )
        .switchIfEmpty(response404);
                    
    }    
}
