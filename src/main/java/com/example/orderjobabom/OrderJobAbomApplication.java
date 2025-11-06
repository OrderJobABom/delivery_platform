package com.example.orderjobabom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class OrderJobAbomApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderJobAbomApplication.class, args);
    }

}
