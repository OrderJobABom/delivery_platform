package com.example.orderjobabom.review.domain;

import com.example.orderjobabom.order.domain.Order;
import com.example.orderjobabom.order.domain.OrderId;
import com.example.orderjobabom.order.domain.OrderRepository;
import com.example.orderjobabom.review.domain.exception.ReviewAlreadyDeletedException;
import com.example.orderjobabom.review.domain.exception.ReviewAlreadyExistsException;
import com.example.orderjobabom.review.domain.exception.ReviewNotEditableException;
import com.example.orderjobabom.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReviewPolicy {

    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    /** 리뷰 작성 가능 여부 검증 */
    public void validateCanWriteReview(OrderId orderId, UUID currentUserId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));

        if (!order.getOrderer().getId().getId().equals(currentUserId)) {
            throw new ReviewNotEditableException();
        }

        reviewRepository.findByOrderId(orderId).ifPresent(existing -> {
            switch (existing.getStatus()) {
                case ACTIVE -> throw new ReviewAlreadyExistsException();
                case DELETED -> throw new ReviewAlreadyDeletedException();
            }
        });
    }
}
