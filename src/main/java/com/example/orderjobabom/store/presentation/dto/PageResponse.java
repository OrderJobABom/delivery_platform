package com.example.orderjobabom.store.presentation.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(
        List<T> content,

        int page, // 1 기반
        int size,
        long totalElements, // 전체 요소 수
        int totalPages,
        boolean hasNext
) {
    public static <T> PageResponse<T> of(Page<T> page) {

        return new PageResponse<>(
                page.getContent(),
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext());
    }


}
