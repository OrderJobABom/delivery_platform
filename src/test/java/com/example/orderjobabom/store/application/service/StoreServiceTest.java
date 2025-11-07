package com.example.orderjobabom.store.application.service;

import com.example.orderjobabom.menu.domain.ItemStatus;
import com.example.orderjobabom.store.application.service.dto.ItemDto;
import com.example.orderjobabom.store.domain.*;
import com.example.orderjobabom.store.presentation.dto.CategoryDto;
import com.example.orderjobabom.store.presentation.dto.ItemOptionRequest;
import com.example.orderjobabom.store.presentation.dto.ItemRequest;
import com.example.orderjobabom.store.presentation.dto.StoreRequest;
import com.example.orderjobabom.user.test.MockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static java.time.DayOfWeek.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class StoreServiceTest {

    @Autowired
    StoreCreateService createService;

    @Autowired
    StoreUpdateService updateService;

    @Autowired
    StoreItemCreateService itemCreateService;

    @Autowired
    MenuAiRecommend aiRecommend;

    StoreRequest request;

    @Autowired
    StoreRepository repository;

    @BeforeEach
    void init() {
        request = StoreRequest.builder()
                .storeName("테스트 매장")
                .storeAddress("인천광역시 계양구 임학안로 28번길 10")
                .storeTel("02-100-1000")
                .startHour(LocalTime.of(10, 0))
                .endHour(LocalTime.of(19, 0))
                .weekdays(List.of(MONDAY,TUESDAY, WEDNESDAY))
                .category(List.of(new CategoryDto(Category.KOREAN, true), new CategoryDto(Category.CHINESE, true)))
                .build();
    }


    @Test
    @Transactional
    @DisplayName("상점 등록 테스트")
    @MockUser(roles = "OWNER")
    void createStoreTest() {

        assertDoesNotThrow(() -> {
            StoreId storeId = createService.create(request);

            Store store = repository.findById(storeId).orElseThrow();
            System.out.println(store);
        });
    }


    @Test
    @Transactional
    @DisplayName("상점 수정 테스트")
    @MockUser(roles = "OWNER")
    void updateStoreTest() {
        StoreId storeId = createService.create(request);

        UUID id = storeId.getId();
        updateService.updateInfo(id, "(수정)" + request.storeName(), "031-1000-1000", request.category());
        updateService.updateAddressInfo(id, "(수정)주소");
        updateService.updateOperatingInfo(id, LocalTime.of(12,0), LocalTime.of(23,0), List.of(MONDAY,TUESDAY));

        Store store = repository.findById(storeId).orElseThrow();
        System.out.println(store);
    }

    @Test
    @DisplayName("매장 메뉴 생성 테스트")
    @MockUser(roles = "OWNER")
    void storeItemCreateTest() {
        StoreId storeId = createService.create(request);

        ItemRequest req = ItemRequest.builder()
                        .name("매운닭발")
                                .category(Category.KOREAN)
                                        .price(10000)
                                                .status(ItemStatus.IN_STOCK)
                                                        .stock(1000)
                                                                .itemOptions(List.of(new ItemOptionRequest("우유 추가",1000)))
                                                                        .genAi(true)
                                                                                .build();

        ItemDto itemDto = itemCreateService.create(storeId.getId(), req);
        System.out.println(itemDto);
    }
}
