package com.example.orderjobabom.store.presentation.controller;

import com.example.orderjobabom.global.presentation.CustomResponse;
import com.example.orderjobabom.store.application.service.StoreOrderQueryService;
import com.example.orderjobabom.store.application.service.dto.StoreOrderSummaryDto;
import com.example.orderjobabom.store.domain.exception.StoreItemSuccessCode;
import com.example.orderjobabom.store.domain.exception.StoreOrderSuccessCode;
import com.example.orderjobabom.store.presentation.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("v1/store/")
@RequiredArgsConstructor
public class StoreController {

    private final StoreOrderQueryService storeOrderQueryService;

    @GetMapping("/orders/{storeId}")
    public CustomResponse<?> getStoreOrders(
            @PathVariable("storeId") UUID storeId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<StoreOrderSummaryDto> storeOrders = storeOrderQueryService.getStoreOrders(storeId, page, size);
        return CustomResponse.of(StoreOrderSuccessCode.STORE_ORDER_OK, PageResponse.of(storeOrders));
    }
}
