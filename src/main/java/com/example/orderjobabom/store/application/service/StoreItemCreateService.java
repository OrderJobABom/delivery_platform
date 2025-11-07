package com.example.orderjobabom.store.application.service;

import com.example.orderjobabom.global.infrastructure.persistence.Price;
import com.example.orderjobabom.global.presentation.exception.FailException;
import com.example.orderjobabom.menu.domain.Item;
import com.example.orderjobabom.menu.domain.ItemOption;
import com.example.orderjobabom.menu.domain.ItemRepository;
import com.example.orderjobabom.menu.domain.Stock;
import com.example.orderjobabom.store.application.service.dto.ItemDto;
import com.example.orderjobabom.store.application.service.dto.ItemOptionDto;
import com.example.orderjobabom.store.domain.MenuAiRecommend;
import com.example.orderjobabom.store.domain.Store;
import com.example.orderjobabom.store.domain.StoreId;
import com.example.orderjobabom.store.domain.StoreRepository;
import com.example.orderjobabom.store.domain.exception.StoreErrorCode;
import com.example.orderjobabom.store.presentation.dto.ItemRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreItemCreateService {

    private final StoreRepository storeRepository;
    private final ItemRepository itemRepository;
    private final MenuAiRecommend aiRecommend;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    public ItemDto create(UUID storeId, ItemRequest itemRequest) {
        StoreId id = StoreId.of(storeId);

        Store store = storeRepository.findById(id).orElseThrow(() -> new FailException(StoreErrorCode.STORE_NOT_FOUND));

        List<ItemOption> itemOptions = itemRequest.itemOptions() == null ? null : itemRequest.itemOptions().stream()
                .map(op -> new ItemOption(op.optionName(), new Price(toInt(op.addPrice())))).toList();

        boolean genAi = itemRequest.genAi() == null ? false : itemRequest.genAi();

        Item newItem = store.createItem(itemRequest.category(), new Price(toInt(itemRequest.price())), itemRequest.name(), itemRequest.status(), new Stock(toInt(itemRequest.stock())), itemOptions, genAi ? aiRecommend : null);
        itemRepository.save(newItem);

        List<ItemOptionDto> optionsDto = newItem.getItemOptions() == null ? null : newItem.getItemOptions().stream()
                .map(op -> new ItemOptionDto(op.getOptionName(), op.getAddPrice().getValue())).toList();

        return ItemDto.builder()
                .id(newItem.getId().getId())
                .price(newItem.getPrice().getValue())
                .name(newItem.getName())
                .outOfStock(newItem.isOutOfStock())
                .options(optionsDto)
                .createdAt(newItem.getCreatedAt())
                .build();
    }

    private int toInt(Integer num) {
        return num == null ? 0 : num;
    }
}
