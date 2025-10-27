package com.olegf.spingapp.thealthbackend;

import org.springframework.boot.SpringApplication;

public class TestTHealthBackendApplication {

    public static void main(String[] args) {
        SpringApplication.from(THealthBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
