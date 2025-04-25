package com.thymeleaf.thymeleaf_reactive_example.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thymeleaf.thymeleaf_reactive_example.persistence.repository.ProductRepository;
import com.thymeleaf.thymeleaf_reactive_example.presentation.dto.ProductDto;
import com.thymeleaf.thymeleaf_reactive_example.service.interfaces.ICommonService;

import reactor.core.publisher.Flux;

@Service
public class ProductService implements ICommonService<ProductDto> {

    @Autowired
    private ProductRepository repository;

    public Flux<ProductDto> findAll() {
        Flux<ProductDto> flux1 = this.repository.findAll()
        .map(p -> {
            return new ProductDto(p.getId(), p.getConcept(), p.getAmount());
        });
        Flux<ProductDto> flux2 = this.repository.findOthers()
        .map(p -> {
            return new ProductDto(p.getId(), p.getConcept(), p.getAmount());
        });

        return Flux.merge(flux1, flux2);
    }
    
}
