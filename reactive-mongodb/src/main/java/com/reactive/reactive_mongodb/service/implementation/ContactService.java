package com.reactive.reactive_mongodb.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reactive.reactive_mongodb.persistence.entity.ContactEntity;
import com.reactive.reactive_mongodb.persistence.repository.IContactRepository;
import com.reactive.reactive_mongodb.presentation.advice.custom.InfoNotFoundException;
import com.reactive.reactive_mongodb.presentation.dto.ContactDto;
import com.reactive.reactive_mongodb.presentation.dto.ContactInsertDto;
import com.reactive.reactive_mongodb.presentation.dto.ContactUpdateDto;
import com.reactive.reactive_mongodb.service.interfaces.ICommonService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ContactService implements ICommonService<ContactDto, ContactInsertDto, ContactUpdateDto> {

    @Autowired
    private IContactRepository repository;

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
        Flux<ContactDto> contacts = repository.findAll()
            .map(c -> {
                return new ContactDto(c.getId(), c.getName(), c.getEmail(), c.getPhone());
            });

        return contacts;
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ContactDto> findById(String id) {
        Mono<ContactDto> contact = repository.findById(id)
            .switchIfEmpty(
                Mono.error(new InfoNotFoundException("No se encontró el contacto con ID: " + id))
            )
            .map(this::convertToDto);

        return contact;
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ContactDto> findByEmail(String email) {
        Mono<ContactDto> contact = repository.findByEmail(email)
            .switchIfEmpty(
                Mono.error(new InfoNotFoundException("No se encontró el contacto con email: " + email))
            )
            .map(this::convertToDto);

        return contact;
    }

    @Override
    @Transactional
    public Mono<ContactDto> save(ContactInsertDto insertDto) {
        ContactEntity entity = this.convertToEntity(insertDto);
        return repository.save(entity)
            .map(this::convertToDto)
            .onErrorMap(e -> new UnsupportedOperationException("No fue posible guardar el contacto: " + insertDto + " ---> e: " + e.toString()));
    }

    @Override
    @Transactional
    public Mono<ContactDto> update(ContactUpdateDto updateDto, String id) {
        Mono<ContactEntity> entity = (id != null && !id.isBlank())
            ? repository.findById(id)
                .switchIfEmpty(
                    Mono.error(new InfoNotFoundException("No se encontró el contacto con ID: " + id))
                )
            : repository.findById(updateDto.getId())
                .switchIfEmpty(
                    Mono.error(new InfoNotFoundException("No se encontró el contacto con ID: " + updateDto.getId()))
                );

        Mono<ContactDto> dto = entity.flatMap(contact -> {
            Optional.ofNullable(updateDto.getName()).ifPresent(contact::setName);
            Optional.ofNullable(updateDto.getEmail()).ifPresent(contact::setEmail);
            Optional.ofNullable(updateDto.getPhone()).ifPresent(contact::setPhone);
            
            return repository.save(contact)
                .map(this::convertToDto);
            // return contact;
        })
        // .map(this::convertToDto)
        .onErrorMap(e -> 
            new UnsupportedOperationException("Error al actualizar el contacto: " + e.getMessage(), e)
        );
        
        return dto;
    }

    @Override
    @Transactional
    public Mono<ContactDto> deleteById(String id) {
        return this.findById(id)
            .flatMap(dto -> 
                repository.deleteById(id)
                    .thenReturn(dto)  
            )
            .log("Deleted Contact");
    }

    @Override
    public Flux<ContactDto> findAllByPhoneOrName(String phoneOrName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllByPhoneOrName'");
    }
    
}
