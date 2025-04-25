package com.project.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Flux;

public class TestUser {
    
    private static final Logger log = LoggerFactory.getLogger(TestUser.class);

    public static void main(String[] args) {
        Flux<String> names = Flux.just(
            "Hugo Santana",
            "Paco Torres",
            "Luis Vega",
            "Zoe Gutierrez",
            "Sandra Aguilar",
            "Ashley Flores",
            "Isaac Serratos"
        );
        Flux<UserEntity> users = names.map(name -> new UserEntity(name.split(" ")[0].toUpperCase(), name.split(" ")[1].toUpperCase()))
        .filter(user -> user.getLastName().endsWith("ES"))
        .doOnNext(user -> {
            if (user == null) {
                throw new RuntimeException("Los nombre no puede estar vacíos");
            }
            System.out.println(user.getName().concat(" ").concat(user.getLastName()));
        })
        .map(user -> {
            String name = user.getName().toLowerCase();
            user.setName(name);
            return user;
        });

        users.subscribe(
            e -> log.info(e.toString()),
            err -> log.error(err.toString()),
            new Runnable() {
                @Override
                public void run() {
                    log.info("Se ha finalizado la ejecución del Observable con éxito"); 
                }
            }
        );
    }
    
}
