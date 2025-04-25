package com.thymeleaf.thymeleaf_reactive_example.service.interfaces;

import reactor.core.publisher.Flux;

public interface ICommonService<T> {
    
    Flux<T> findAll();

}
