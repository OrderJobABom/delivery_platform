package com.example.orderjobabom.store.application.service;

import com.example.orderjobabom.menu.domain.ItemStatus;
import com.example.orderjobabom.store.application.service.dto.ItemDto;
import com.example.orderjobabom.store.domain.*;
import com.example.orderjobabom.store.domain.dto.StoreCreateDto;
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
import static org.junit.jupiter.api.Assertions.*;

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
    @Autowired
    private StoreDeleteService storeDeleteService;

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
    @DisplayName("서비스: 매장 생성")
    @MockUser(roles = "OWNER")
    void createStore() {
        StoreCreateDto created = createService.create(request);
        StoreId storeId = created.storeId();

        Store store = repository.findById(storeId).orElseThrow();
        assertEquals("테스트 매장", store.getStoreName());
        assertEquals("02-100-1000", store.getStoreTel());
        assertEquals(List.of(MONDAY, TUESDAY, WEDNESDAY), store.getOperatingInfo().getWeekdays());
        assertNotNull(store.getCategories());
        assertTrue(store.getCategories().size() >= 1);
    }


    @Test
    @Transactional
    @DisplayName("상점 수정 테스트")
    @MockUser(roles = "OWNER")
    void updateStore() {
        StoreCreateDto created = createService.create(request);
        StoreId storeId = created.storeId();
        UUID id = storeId.getId();

        // when
        updateService.updateInfo(id, "(수정)테스트 매장", "031-100-1000", request.category());
        updateService.updateAddressInfo(id, "(수정)인천광역시 계양구 임학안로 28번길 10");
        updateService.updateOperatingInfo(id, LocalTime.of(11, 0), LocalTime.of(23, 0), List.of(MONDAY, WEDNESDAY));

        // then
        Store store = repository.findById(storeId).orElseThrow();
        assertTrue(store.getStoreName().startsWith("(수정)"));
        assertEquals("031-100-1000", store.getStoreTel());
        assertEquals(List.of(MONDAY, WEDNESDAY), store.getOperatingInfo().getWeekdays());
    }


    @Test
    @DisplayName("매장 메뉴 생성 테스트")
    @MockUser(roles = "OWNER")
    void storeItemCreateTest() {
        StoreId storeId = createService.create(request).storeId();

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
