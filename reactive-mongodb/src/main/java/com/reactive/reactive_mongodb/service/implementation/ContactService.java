package com.reactive.reactive_mongodb.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reactive.reactive_mongodb.persistence.entity.ContactEntity;
import com.reactive.reactive_mongodb.persistence.repository.IContactRespository;
import com.reactive.reactive_mongodb.presentation.dto.ContactDto;
import com.reactive.reactive_mongodb.presentation.dto.ContactInsertDto;
import com.reactive.reactive_mongodb.service.interfaces.ICommonService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ContactService implements ICommonService<ContactDto, ContactInsertDto> {

    @Autowired
    private IContactRespository respository;

    private ContactDto convertToDto(ContactEntity entity) {
        return new ContactDto(
            entity.getId(), 
            entity.getName(), 
            entity.getEmail(), 
            entity.getPhone()
        );
    }

    private ContactEntity convertToEntity(ContactInsertDto insertDto) {
        return new ContactEntity(
            insertDto.getName(),
            insertDto.getEmail(),
            insertDto.getPhone()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ContactDto> findAll() {
        Flux<ContactDto> contacts = respository.findAll()
            .map(c -> {
                return new ContactDto(c.getId(), c.getName(), c.getEmail(), c.getPhone());
            });

        return contacts;
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ContactDto> findById(String id) {
        Mono<ContactDto> contact = respository.findById(id)
            .switchIfEmpty(
                Mono.error(new RuntimeException("No se encontró el contacto con ID: " + id))
            )
            .map(this::convertToDto);

        return contact;
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ContactDto> findByEmail(String email) {
        Mono<ContactDto> contact = respository.findByEmail(email)
            .switchIfEmpty(
                Mono.error(new RuntimeException("No se encontró el contacto con email: " + email))
            )
            .map(this::convertToDto);

        return contact;
    }

    @Override
    @Transactional
    public Mono<ContactDto> save(ContactInsertDto insertDto) {
        ContactEntity entity = this.convertToEntity(insertDto);
        return respository.save(entity)
            .map(this::convertToDto)
            .onErrorMap(e -> new UnsupportedOperationException("No fue posible guardar el contacto: " + insertDto + " ---> e: " + e.toString()));
    }

    @Override
    public Flux<ContactDto> findAllByPhoneOrName(String phoneOrName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllByPhoneOrName'");
    }
    
}
