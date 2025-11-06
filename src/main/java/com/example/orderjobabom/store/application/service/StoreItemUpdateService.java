package com.example.orderjobabom.store.application.service;

import com.example.orderjobabom.global.presentation.exception.FailException;
import com.example.orderjobabom.menu.domain.Item;
import com.example.orderjobabom.menu.domain.ItemId;
import com.example.orderjobabom.menu.domain.ItemRepository;
import com.example.orderjobabom.menu.domain.exception.ItemErrorCode;
import com.example.orderjobabom.store.application.service.dto.ItemOptionDto;
import com.example.orderjobabom.store.domain.Store;
import com.example.orderjobabom.store.domain.StoreId;
import com.example.orderjobabom.store.domain.StoreRepository;
import com.example.orderjobabom.store.domain.exception.StoreErrorCode;
import com.example.orderjobabom.store.presentation.dto.ItemRequest;
import com.example.orderjobabom.store.presentation.dto.UpdateItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreItemUpdateService {

    private final StoreRepository storeRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    public UpdateItemResponse update(UUID storeId, UUID itemId, ItemRequest dto) {

        Store store = storeRepository.findById(StoreId.of(storeId)).orElseThrow(() -> new FailException(StoreErrorCode.STORE_NOT_FOUND));
        Item item = itemRepository.findById(ItemId.of(itemId)).orElseThrow(() -> new FailException(ItemErrorCode.ITEM_NOT_FOUND));

        Item updateItem = store.updateItem(item, dto);
        itemRepository.save(updateItem);

        List<ItemOptionDto> options = updateItem.getItemOptions().stream().map(op -> new ItemOptionDto(op.getOptionName(), op.getAddPrice().getValue())).toList();

        return UpdateItemResponse.builder()
                .storeId(storeId)
                .itemId(updateItem.getId().getId())
                .price(updateItem.getPrice().getValue())
                .name(updateItem.getName())
                .status(updateItem.getItemStatus())
                .active(updateItem.isActive())
                .stock(updateItem.getStock().getValue())
                .options(options)
                .build();

    }

}
