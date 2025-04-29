package com.reactive.reactive_mongodb.presentation.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reactive.reactive_mongodb.presentation.dto.ContactDto;
import com.reactive.reactive_mongodb.presentation.dto.ContactInsertDto;
import com.reactive.reactive_mongodb.presentation.dto.ContactUpdateDto;
import com.reactive.reactive_mongodb.service.interfaces.ICommonService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/contacts")
public class ContactController {

    @Autowired
    private ICommonService<ContactDto, ContactInsertDto, ContactUpdateDto> service;

    @GetMapping
    public Flux<ContactDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ContactDto>> findById(@PathVariable String id) {
        if (id.isBlank()) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        return service.findById(id)
            .map(c -> ResponseEntity.ok(c))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public Mono<ResponseEntity<ContactDto>> findByEmail(@PathVariable String email) {
        if (email.isBlank()) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        return service.findByEmail(email)
            .map(c -> ResponseEntity.ok(c))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<ContactDto>> save(@RequestBody ContactInsertDto insertDto) {
        return service.save(insertDto)
            .map(c -> ResponseEntity
                .created(URI.create("/api/v1/contacts/" + c.getId()))
                .body(c)
            );
    }

    @PutMapping({"/{id}", ""})
    public Mono<ResponseEntity<ContactDto>> update(@RequestBody ContactUpdateDto updateDto, 
        @PathVariable(required = false) String id) {
        // if (id.isBlank()) {
        //     return Mono.just(ResponseEntity.badRequest().build());
        // }
        return service.update(updateDto, id)
            .map(c -> ResponseEntity.ok(c))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<ContactDto>> deleteById(@PathVariable String id) {
        if (id.isBlank()) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        return service.deleteById(id)
            .map(c -> ResponseEntity.ok(c))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
}
