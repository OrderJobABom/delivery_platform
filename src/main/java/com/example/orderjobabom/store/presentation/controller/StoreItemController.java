package com.example.orderjobabom.store.presentation.controller;

import com.example.orderjobabom.global.presentation.CustomResponse;
import com.example.orderjobabom.store.application.service.StoreItemCreateService;
import com.example.orderjobabom.store.application.service.StoreItemDeleteService;
import com.example.orderjobabom.store.application.service.StoreItemUpdateService;
import com.example.orderjobabom.store.application.service.dto.ItemDto;
import com.example.orderjobabom.store.domain.exception.StoreItemSuccessCode;
import com.example.orderjobabom.store.presentation.dto.DeleteItemResponse;
import com.example.orderjobabom.store.presentation.dto.ItemRequest;
import com.example.orderjobabom.store.presentation.dto.UpdateItemResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/storeItem")
@RequiredArgsConstructor
@Tag(name = "메뉴 API", description = "메뉴 생성, 조회, 취소 관련 API")

public class StoreItemController {

    private final StoreItemCreateService storeItemCreateService;
    private final StoreItemUpdateService storeItemUpdateService;
    private final StoreItemDeleteService storeItemDeleteService;

    @PostMapping("/menu/{storeId}")
    public CustomResponse<?> createStoreItem(
            @PathVariable("storeId") UUID storeId,
            @RequestBody ItemRequest itemRequest) {

        ItemDto itemDto = storeItemCreateService.create(storeId, itemRequest);
        return CustomResponse.of(StoreItemSuccessCode.ITEM_CREATE_SUCCESS, itemDto);

    }

    @PutMapping("/menu/{storeId}/{itemId}")
    public CustomResponse<?> updateStoreItem(
            @PathVariable("storeId") UUID storeId,
            @PathVariable("itemId") UUID itemId,
            @RequestBody ItemRequest itemRequest
    ) {

        UpdateItemResponse updateResponse = storeItemUpdateService.update(storeId, itemId, itemRequest);
        return CustomResponse.of(StoreItemSuccessCode.ITEM_UPDATE_SUCCESS, updateResponse);
    }

    @DeleteMapping("/menu/{storeId}/{itemId}")
    public CustomResponse<?> deleteStoreItem(
            @PathVariable("storeId") UUID storeId,
            @PathVariable("itemId") UUID itemId
    ) {

        DeleteItemResponse deleteItemResponse = storeItemDeleteService.deleteItem(storeId, itemId);
        return CustomResponse.of(StoreItemSuccessCode.ITEM_DELETE_SUCCESS, deleteItemResponse);

    }
}
