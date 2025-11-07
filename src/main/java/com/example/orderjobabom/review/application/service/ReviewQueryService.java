package com.example.orderjobabom.review.application.service;

import com.example.orderjobabom.review.domain.Review;
import com.example.orderjobabom.review.domain.ReviewRepository;
import com.example.orderjobabom.review.presentation.dto.ReviewResponse;
import com.example.orderjobabom.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewQueryService {

    private final ReviewRepository reviewRepository;


     // 로그인한 유저의 리뷰 목록 조회

    public List<ReviewResponse> findMyReviews(UUID userUuid) {
        UserId userId = new UserId(userUuid);
        List<Review> reviews = reviewRepository.findActiveByUserId(userId.getId());
        return reviews.stream()
                .map(ReviewResponse::from)
                .collect(Collectors.toList());
    }
}
