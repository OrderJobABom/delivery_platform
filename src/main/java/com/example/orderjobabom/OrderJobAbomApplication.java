package com.example.orderjobabom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;


@SpringBootApplication
@ConfigurationPropertiesScan("com.example.orderjobabom.global.infrastructure.gemini")
public class OrderJobAbomApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderJobAbomApplication.class, args);
    }

}
