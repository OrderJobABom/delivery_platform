package com.example.orderjobabom.store.presentation.controller;

import com.example.orderjobabom.global.presentation.CustomResponse;
import com.example.orderjobabom.store.application.service.StoreCreateService;
import com.example.orderjobabom.store.application.service.StoreDeleteService;
import com.example.orderjobabom.store.application.service.StoreOrderQueryService;
import com.example.orderjobabom.store.application.service.StoreUpdateService;
import com.example.orderjobabom.store.application.service.dto.StoreOrderSummaryDto;
import com.example.orderjobabom.store.domain.dto.StoreCreateDto;
import com.example.orderjobabom.store.domain.exception.StoreOrderSuccessCode;
import com.example.orderjobabom.store.domain.exception.StoreSuccessCode;
import com.example.orderjobabom.store.presentation.dto.PageResponse;
import com.example.orderjobabom.store.presentation.dto.StoreRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("v1/owner/stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreCreateService storeCreateService;
    private final StoreUpdateService storeUpdateService;
    private final StoreDeleteService storeDeleteService;
    private final StoreOrderQueryService storeOrderQueryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomResponse<StoreCreateDto> createStore(@Valid @RequestBody StoreRequest request) {
        StoreCreateDto storeIdResponse = storeCreateService.create(request);

        return CustomResponse.of(StoreSuccessCode.STORE_CREATED, storeIdResponse);
    }

    @PatchMapping("/{storeId}")
    public void updateStore(@PathVariable("storeId") UUID storeId, @Valid @RequestBody StoreRequest request) {
        storeUpdateService.updateInfo(storeId, request.storeName(), request.storeTel(), request.category());
        storeUpdateService.updateOperatingInfo(storeId, request.startHour(), request.endHour(), request.weekdays());
        storeUpdateService.updateAddressInfo(storeId, request.storeAddress());
    }

    @DeleteMapping("/{storeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStore(@PathVariable("storeId") UUID storeId) {
        storeDeleteService.delete(storeId);
    }

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
