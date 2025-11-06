package com.example.orderjobabom.store.presentation.controller;

import com.example.orderjobabom.store.application.service.StoreCreateService;
import com.example.orderjobabom.store.application.service.StoreDeleteService;
import com.example.orderjobabom.store.application.service.StoreUpdateService;
import com.example.orderjobabom.store.domain.StoreId;
import com.example.orderjobabom.store.presentation.dto.StoreRequest;
import com.example.orderjobabom.store.presentation.dto.StoreResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StoreResponse createStore(@Valid @RequestBody StoreRequest request) {
        StoreId storeId = storeCreateService.create(request);

        return new StoreResponse(storeId);
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

}
