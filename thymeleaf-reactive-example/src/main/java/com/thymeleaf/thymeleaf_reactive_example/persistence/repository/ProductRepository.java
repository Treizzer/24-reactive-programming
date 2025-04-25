package com.thymeleaf.thymeleaf_reactive_example.persistence.repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.thymeleaf.thymeleaf_reactive_example.persistence.entity.ProductEntity;

import reactor.core.publisher.Flux;

@Repository
public class ProductRepository {
    
    private static List<ProductEntity> list = new ArrayList<>();

    private static List<ProductEntity> list2 = new ArrayList<>();
    
    static {
        list.add(new ProductEntity(1, "Ordenador", 200));
        list.add(new ProductEntity(2, "Tablet", 300));
        list.add(new ProductEntity(3, "Audicular", 200));
        
        list2.add(new ProductEntity(4, "Movil", 200));
        list2.add(new ProductEntity(5, "Teclado", 30));
        list2.add(new ProductEntity(6, "Raton", 20));
    }

    public Flux<ProductEntity> findAll() {
        return Flux.fromIterable(list)
            .delayElements(Duration.ofSeconds(3));
    }

    public Flux<ProductEntity> findOthers() {
        return Flux.fromIterable(list2)
            .delayElements(Duration.ofSeconds(3));
    }

}
