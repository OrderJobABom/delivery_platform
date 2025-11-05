package com.example.orderjobabom.menu.domain;

import com.example.orderjobabom.global.infrastructure.converter.PriceConverter;
import com.example.orderjobabom.global.infrastructure.persistence.BaseEntity;
import com.example.orderjobabom.global.infrastructure.persistence.Price;
import com.example.orderjobabom.global.presentation.exception.FailException;
import com.example.orderjobabom.menu.domain.exception.ItemErrorCode;
import com.example.orderjobabom.menu.infrastructure.persistence.converter.StockConverter;
import com.example.orderjobabom.menu.presentation.dto.request.UpdateItemRequestDTO;
import com.example.orderjobabom.store.domain.Category;
import com.example.orderjobabom.store.domain.StoreId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Table(name = "p_item")
@Access(AccessType.FIELD)
public class Item extends BaseEntity {

    @EmbeddedId
    private ItemId id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(nullable = false)),
    })
    private StoreId storeId;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private Category category;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "P_ITEM_OPTION", joinColumns = @JoinColumn(name = "item_id"))
    @OrderColumn(name = "option_idx")
    private List<ItemOption> itemOptions;

    @Convert(converter = PriceConverter.class)
    private Price price;

    @Column(nullable = false)
    private String name;

    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    @Convert(converter = StockConverter.class)
    @Column(name = "stock")
    private Stock stock;

    @Transient
    private boolean outOfStock; // 품절 상태 확인

    @Builder
    public Item(StoreId storeId, ItemId itemId, Category category, List<ItemOption> itemOptions, Price price, String name, ItemStatus status, Stock stock) {
        this.storeId = storeId;
        this.id = Objects.requireNonNullElse(id, ItemId.of());
        this.category = category;
        this.itemOptions = itemOptions;
        this.price = price;
        this.name = name;
        this.itemStatus = Objects.requireNonNullElse(status, ItemStatus.UNLIMITED_STOCK);
        setStock(stock);
    }

    // 재고 등록
    private void setStock(Stock stock) {
        this.stock = stock;

        if (stock.getValue() == 0 || itemStatus == ItemStatus.OUT_OF_STOCK) {
            outOfStock = true;
        }
    }

    // 옵션 추가
    public void addOption(ItemOption option) {
        itemOptions = Objects.requireNonNullElseGet(this.itemOptions, () -> new ArrayList<ItemOption>());
        itemOptions.add(option);
    }

    // 일반 옵션 제거
    public void removeOption(ItemOption option) {
        if (option == null) return;
        itemOptions.remove(option);
    }

    // 순서 번호를 이용한 옵션 제거
    public void removeOption(int idx) {
        if (itemOptions == null) return;
        itemOptions.remove(idx);
    }

    // 옵션 전체 제거
    public void removeAllOptions() {
        if (itemOptions == null) return;
        itemOptions.clear();
    }

    /**
     * 옵션을 포함한 전체 가격
     * @param itemCnt - 상품 수량
     * @param options - <옵션 번호, 수량>
     * @return
     */

    public Price getPrice(int itemCnt, Map<Integer, Integer> options) {

        Price totalPrice = price.add(price.multiply(itemCnt));
        if (itemOptions != null && options != null) {

            options.forEach((optionIdx, optionCnt) -> {
                ItemOption itemOption = itemOptions.get(optionIdx);
                if (itemOption == null) return;
                totalPrice.add(itemOption.getAddPrice().multiply(optionCnt));
            });
        }

        return totalPrice;
    }

    // 상품 삭제
    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    // 상품 등록 여부
    public static void exists(ItemId itemId, ItemRepository itemRepository) {
        if (!itemRepository.existsById(itemId)) {
            throw new FailException(ItemErrorCode.ITEM_NOT_FOUND);
        }
    }

    // 상품 수정
    public void updateItem(UpdateItemRequestDTO itemRequestDTO, ItemRepository itemRepository) {

        exists(ItemId.of(itemRequestDTO.itemId()), itemRepository);

        this.category = itemRequestDTO.category();
        this.price = itemRequestDTO.price();
        this.name = itemRequestDTO.name();
        this.stock = itemRequestDTO.stock();
        this.itemOptions = itemRequestDTO.itemOptions();
    }
}
