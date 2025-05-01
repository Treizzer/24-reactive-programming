package com.reactive.reactive_mongodb.persistence.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.reactive.reactive_mongodb.persistence.entity.ContactEntity;

import reactor.core.publisher.Mono;



@Repository
public interface IContactRepository extends ReactiveMongoRepository<ContactEntity, String> {

    Mono<ContactEntity> findByEmail(String email);

    Mono<ContactEntity> findAllByPhoneOrName(String phoneOrName);

}
