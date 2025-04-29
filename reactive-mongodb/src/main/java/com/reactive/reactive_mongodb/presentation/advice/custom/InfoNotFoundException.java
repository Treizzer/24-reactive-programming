package com.reactive.reactive_mongodb.presentation.advice.custom;

public class InfoNotFoundException extends RuntimeException {

    public InfoNotFoundException(String message) {
        super(message);
    }
    
}
