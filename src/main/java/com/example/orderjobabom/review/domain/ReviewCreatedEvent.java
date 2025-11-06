package com.example.orderjobabom.review.domain;

import com.example.orderjobabom.store.domain.StoreId;
import java.util.UUID;

public record ReviewCreatedEvent(
        StoreId storeId,
        UUID reviewId
) {}
