package com.olegf.spingapp.thealthbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@ConfigurationPropertiesScan
public class THealthBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(THealthBackendApplication.class, args);
    }

}
