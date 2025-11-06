package com.example.orderjobabom.store.application.service;

import com.example.orderjobabom.global.presentation.exception.FailException;
import com.example.orderjobabom.menu.domain.Item;
import com.example.orderjobabom.menu.domain.ItemId;
import com.example.orderjobabom.menu.domain.ItemRepository;
import com.example.orderjobabom.menu.domain.exception.ItemErrorCode;
import com.example.orderjobabom.store.domain.StoreId;
import com.example.orderjobabom.store.domain.StoreRepository;
import com.example.orderjobabom.store.domain.exception.StoreErrorCode;
import com.example.orderjobabom.store.presentation.dto.DeleteItemResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreItemDeleteService {

    private final StoreRepository storeRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    public DeleteItemResponse deleteItem(UUID storeId, UUID itemId) {

        StoreId findStoreId = StoreId.of(storeId);
        ItemId findItemId = ItemId.of(itemId);

        storeRepository.findById(findStoreId).orElseThrow(() -> new FailException(StoreErrorCode.STORE_NOT_FOUND));
        Item item = itemRepository.findById(findItemId).orElseThrow(() -> new FailException(ItemErrorCode.ITEM_NOT_FOUND));

        if (!item.getStoreId().equals(findStoreId)) {
            throw new FailException(ItemErrorCode.ITEM_NOT_BELONG);
        }

        DeleteItemResponse deleteResponse = DeleteItemResponse.builder()
                .id(item.getId().getId())
                .name(item.getName()).build();

        itemRepository.delete(item);

        return deleteResponse;
    }
}
