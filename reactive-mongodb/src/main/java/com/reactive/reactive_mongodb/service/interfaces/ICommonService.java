package com.reactive.reactive_mongodb.service.interfaces;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICommonService<T, TI, TU> {

    Flux<T> findAll();

    Mono<T> findById(String id);

    Mono<T> findByEmail(String email);
    
    Mono<T> save(TI insertDto);

    Mono<T> update(TU updateDto, String id);

    Mono<T> deleteById(String id);

    Flux<T> findAllByPhoneOrName(String phoneOrName);
    
}
