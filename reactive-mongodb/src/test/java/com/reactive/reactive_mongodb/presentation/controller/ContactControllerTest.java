package com.reactive.reactive_mongodb.presentation.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.reactive.reactive_mongodb.presentation.dto.ContactDto;
import com.reactive.reactive_mongodb.presentation.dto.ContactInsertDto;
import com.reactive.reactive_mongodb.presentation.dto.ContactUpdateDto;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureWebTestClient // Async test
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContactControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private ContactDto contactDto;
    private final String email = "web@mail.com";

    @Test
    @Order(0)
    public void testSave() {
        Flux<ContactDto> fluxDto = webTestClient.post()
            .uri("/api/v1/contacts")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new ContactInsertDto("webTest", "web@mail.com", "111")))
            .exchange()
            .expectStatus().isCreated()
            .returnResult(ContactDto.class)
            .getResponseBody()
            .log();

        fluxDto.next().subscribe(contact ->
            this.contactDto = contact
        );

        Assertions.assertNotNull(contactDto);
    }

    @Test
    @Order(1)
    public void testFindByEmail() {
        Flux<ContactDto> fluxDto = webTestClient.get()
            .uri("/api/v1/contacts/email/{email}", email)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .returnResult(ContactDto.class)
            .getResponseBody()
            .log();

        StepVerifier.create(fluxDto)
            .expectSubscription()
            .expectNextMatches(contact -> contact.getEmail().equals(email))
            .verifyComplete();
    }

    @Test
    @Order(2)
    public void testUpdate() {
        Flux<ContactDto> fluxDto = webTestClient.put()
            .uri("/api/v1/contacts/{id}", contactDto.getId())
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new ContactUpdateDto(contactDto.getId(), "WebTestClient", email.concat(".gob"), "222")))
            .exchange()
            .expectStatus().isOk()
            .returnResult(ContactDto.class)
            .getResponseBody()
            .log();

        StepVerifier.create(fluxDto)
            .expectSubscription()
            .expectNextMatches(contact -> contact.getEmail().equals(email.concat(".gob")))
            .verifyComplete();
    }

    @Test
    @Order(3)
    public void testFindAll() {
        Flux<ContactDto> fluxDtos = webTestClient.get()
            .uri("/api/v1/contacts")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .returnResult(ContactDto.class)
            .getResponseBody()
            .log();

        StepVerifier.create(fluxDtos)
            .expectSubscription()
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    @Order(4)
    public void testDeleteById() {
        Flux<ContactDto> fluxDto = webTestClient.delete()
            .uri("/api/v1/contacts/{id}", contactDto.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .returnResult(ContactDto.class)
            .getResponseBody()
            .log();

        StepVerifier.create(fluxDto)
            .expectSubscription()
            .expectNextMatches(contact -> contact.getId().equals(contactDto.getId()))
            .verifyComplete();
    }
    
}
