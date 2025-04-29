package com.reactive.reactive_mongodb.service.interfaces;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICommonService<T, TI> {

    Flux<T> findAll();

    Mono<T> findById(String id);

    Mono<T> findByEmail(String email);
    
    Mono<T> save(TI insertDto);

    Flux<T> findAllByPhoneOrName(String phoneOrName);
    
}
