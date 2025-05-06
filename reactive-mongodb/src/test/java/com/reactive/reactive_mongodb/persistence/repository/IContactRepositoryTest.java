package com.reactive.reactive_mongodb.persistence.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.reactive.reactive_mongodb.persistence.entity.ContactEntity;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IContactRepositoryTest {

    @Autowired
    private IContactRepository repository;

    // @Autowired
    // private ReactiveMongoOperations operations;

    @BeforeAll
    public void testSave() {
        ContactEntity contact1 = new ContactEntity(
            "Hugo", "hugoP@mail.com", "111"
        );
        ContactEntity contact2 = new ContactEntity(
            "Paco", "pacoP@mail.com", "222"
        );
        ContactEntity contact3 = new ContactEntity(
            "Luis", "luisP@mail.com", "333"
        );
        // ContactEntity contact4 = new ContactEntity(
        //     "Zoe", "zoe@mail.com", "444"
        // );
        // ContactEntity contact5 = new ContactEntity(
        //     "Abril", "abril@mail.com", "555"
        // );

        // Save them
        StepVerifier.create(repository.insert(contact1).log())
            .expectSubscription()
            .expectNextCount(1)
            .verifyComplete();

        StepVerifier.create(repository.save(contact2).log())
            .expectSubscription()
            .expectNextCount(1)
            .verifyComplete();

        StepVerifier.create(repository.save(contact3).log())
            .expectSubscription()
            .expectNextMatches(c3 -> c3.getId() != null)
            .verifyComplete();
    }

    @Test
    @Order(1)
    public void textFindAll() {
        StepVerifier.create(repository.findAll().log())
            .expectSubscription()
            .expectNextCount(3)
            .verifyComplete();
    }

    @Test
    @Order(2)
    public void testFindById() {}

    @Test
    @Order(3)
    public void testFindByEmail() {
        String email = "pacoP@mail.com";

        StepVerifier.create(repository.findByEmail(email).log())
            .expectSubscription()
            .expectNextMatches(contact -> contact.getEmail().equals(email))
            .verifyComplete();
    }

    @Test
    @Order(4)
    public void testUpdate() {
        String email = "pacoP@mail.com";
        String phone = "1010";

        // Mono<ContactEntity> updatedContact = repository.findByEmail(email)
        //     .map(contact -> {
        //         contact.setPhone(phone);
        //         return contact;
        //     })
        //     .flatMap(contact -> {
        //         return repository.save(contact);
        //     });
        // StepVerifier.create(updatedContact.log())
        //     .expectSubscription()
        //     .expectNextMatches(contact -> contact.getPhone().equals(phone))
        //     .verifyComplete();
    
        StepVerifier.create(
            repository.findByEmail(email)
                .flatMap(contact -> {
                    contact.setPhone(phone);
                    return repository.save(contact); // Save reactively
                })
                // Retrive to validate update
                .flatMap(updated -> repository.findById(updated.getId()))
                .log()
        )
        .expectSubscription()
        // Validate phone number
        .expectNextMatches(contact -> contact.getPhone().equals(phone))
        .verifyComplete();
    }

    @Test
    @Order(5)
    public void testDeleteById() {
        String email = "pacoP@mail.com";
        // Mono<Void> deleted = repository.findByEmail(email)
        //     .flatMap(contact -> 
        //         repository.deleteById(contact.getId())
        //     )
        //     .log();
        // StepVerifier.create(deleted)
        //     .expectSubscription()
        //     .verifyComplete();
    
        StepVerifier.create(
            repository.findByEmail(email)
                .flatMap(contact -> 
                    repository.deleteById(contact.getId())
                        .then(repository.findById(contact.getId()))
                )
                .log()
        )
        .expectSubscription()
        .expectNextCount(0) // Expect no elements after deletion
        .verifyComplete();
    }

    @Test
    @Order(6)
    public void testDeleteContact() {
        String email = "pacoP@mail.com";

        Mono<Void> deleted = repository.findByEmail(email)
            .flatMap(contact -> 
                repository.delete(contact)
            )
            .log();

        StepVerifier.create(deleted)
            .expectSubscription()
            .verifyComplete();
    }

    @AfterAll
    public void cleanData() {
        Mono<Void> elementsDeleted = repository.deleteAll();

        StepVerifier.create(elementsDeleted.log())
            .expectSubscription()
            .verifyComplete();
    }
    
}






// Suggest
// @SpringBootTest
// @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// public class IContactRepositoryTest {

//     @Autowired
//     private IContactRepository repository;

//     @BeforeEach
//     public void setupData() {
//         repository.deleteAll()
//             .thenMany(repository.saveAll(Flux.just(
//                 new ContactEntity("Hugo", "hugoP@mail.com", "111"),
//                 new ContactEntity("Paco", "pacoP@mail.com", "222"),
//                 new ContactEntity("Luis", "luisP@mail.com", "333")
//             )))
//             .block(); // Clean DB before each test
//     }

//     @Test
//     @Order(1)
//     public void testFindAll() {
//         StepVerifier.create(repository.findAll().log())
//             .expectSubscription()
//             .expectNextCount(3)
//             .verifyComplete();
//     }

//     @Test
//     @Order(3)
//     public void testFindByEmail() {
//         String email = "pacoP@mail.com";

//         StepVerifier.create(repository.findByEmail(email).log())
//             .expectSubscription()
//             .expectNextMatches(contact -> contact.getEmail().equals(email))
//             .verifyComplete();
//     }
// }

