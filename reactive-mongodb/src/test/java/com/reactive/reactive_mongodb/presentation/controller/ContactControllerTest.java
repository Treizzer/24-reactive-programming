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

    // @Test
    // @Order(0)
    // public void testSave() {
    //     Flux<ContactDto> fluxDto = webTestClient.post()
    //         .uri("/api/v1/contacts")
    //         .accept(MediaType.APPLICATION_JSON)
    //         .contentType(MediaType.APPLICATION_JSON)
    //         .body(BodyInserters.fromValue(new ContactInsertDto("webTest", "web@mail.com", "111")))
    //         .exchange()
    //         .expectStatus().isCreated()
    //         .returnResult(ContactDto.class)
    //         .getResponseBody()
    //         .log();
    //     fluxDto.next().subscribe(contact ->
    //         this.contactDto = contact
    //     );
    //     Assertions.assertNotNull(contactDto);
    // }

    @Test
    @Order(0)
    public void testSave() {
        StepVerifier.create(
            webTestClient.post()
                .uri("/api/v1/contacts")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new ContactInsertDto("webTest", email, "111")))
                .exchange()
                .expectStatus().isCreated()
                .returnResult(ContactDto.class)
                .getResponseBody()
                .log()
        )
        .expectSubscription()
        .expectNextMatches(contact -> {
            this.contactDto = contact; // Assign reactively
            return contact.getEmail().equals(email);
        })
        .verifyComplete();

        Assertions.assertNotNull(contactDto); // Ensure is set
    }

    @Test
    @Order(1)
    public void testFindByEmail() {
        StepVerifier.create(
            webTestClient.get()
                .uri("/api/v1/contacts/email/{email}", email)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ContactDto.class)
                .getResponseBody()
                .log()
        )
        .expectSubscription()
        .expectNextMatches(contact -> contact.getEmail().equals(email))
        .verifyComplete();
    }

    @Test
    @Order(2)
    public void testUpdate() {
        StepVerifier.create(
            webTestClient.put()
                .uri("/api/v1/contacts/{id}", contactDto.getId())
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(
                    new ContactUpdateDto(contactDto.getId(), "WebTestClient", email.concat(".gob"), "222")
                ))
                .exchange()
                .expectStatus().isOk()
                .returnResult(ContactDto.class)
                .getResponseBody()
                .log()
        )
        .expectSubscription()
        .expectNextMatches(contact -> contact.getEmail().equals(email.concat(".gob")))
        .verifyComplete();
    }

    @Test
    @Order(3)
    public void testFindAll() {
        StepVerifier.create(
            webTestClient.get()
                .uri("/api/v1/contacts")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .returnResult(ContactDto.class)
                .getResponseBody()
                .log()
        )
        .expectSubscription()
        .expectNextCount(1)
        .verifyComplete();
    }

    @Test
    @Order(4)
    public void testDeleteById() {
        StepVerifier.create(
            webTestClient.delete()
                .uri("/api/v1/contacts/{id}", contactDto.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(ContactDto.class)
                .getResponseBody()
                .log()
        )
        .expectSubscription()
        // A contact is expected if there is more than one, then use expectNextCount()
        .expectNextMatches(contact -> contact.getId().equals(contactDto.getId()))
        .verifyComplete();

        StepVerifier.create(
            webTestClient.get()
                .uri("/api/v1/contacts/{id}", contactDto.getId())  
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .returnResult(Void.class)
                .getResponseBody()
                .single() // Convert to Mono<Void> to align with StepVerifier
                .log()
        )
        .verifyComplete();
    }
    
}
