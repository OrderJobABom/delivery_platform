package com.example.orderjobabom.global.presentation.event;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Events {

    private final ApplicationEventPublisher publisher;
    private static ApplicationEventPublisher staticPublisher;

    @PostConstruct
    void init() {
        staticPublisher = publisher;
    }

    public static void raise(Object event) {
        staticPublisher.publishEvent(event);
    }
}
