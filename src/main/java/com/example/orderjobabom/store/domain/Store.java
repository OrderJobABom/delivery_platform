package com.example.orderjobabom.store.domain;

import com.example.orderjobabom.global.infrastructure.persistence.Price;
import com.example.orderjobabom.global.presentation.exception.FailException;
import com.example.orderjobabom.menu.domain.*;
import com.example.orderjobabom.menu.presentation.dto.request.UpdateItemRequestDTO;
import com.example.orderjobabom.store.domain.exception.StoreErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@Access(AccessType.FIELD)
@Table(name = "P_STORE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {

    @EmbeddedId
    private StoreId id;

    @Column(length = 100, nullable = false)
    private String name;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "P_STORE_CATEGORY", joinColumns = @JoinColumn(name = "store_id"))
    @OrderColumn(name = "category_idx")
    private List<StoreCategory> categories;


    // Item 생성
    public Item createItem(Category category, Price price, String name, ItemStatus itemStatus, Stock stock, List<ItemOption> itemOptions) {

        if (category != null && !categoryExists(category)) {
            throw new FailException(StoreErrorCode.CATEGORY_NOT_FOUND);
        }

        return Item.builder()
                .storeId(id)
                .category(category)
                .status(itemStatus)
                .price(price)
                .name(name)
                .stock(stock)
                .itemOptions(itemOptions)
                .build();

    }

    public boolean categoryExists(Category category) {
        return category != null && categories.stream().anyMatch(ca -> ca.getCategory() == category);

    }

    public void updateItem(UpdateItemRequestDTO dto) {

    }


}
