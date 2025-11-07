package com.example.orderjobabom.store.presentation.controller;

import com.example.orderjobabom.store.application.service.StoreCreateService;
import com.example.orderjobabom.store.application.service.StoreDeleteService;
import com.example.orderjobabom.store.domain.Category;
import com.example.orderjobabom.store.domain.Store;
import com.example.orderjobabom.store.domain.StoreId;
import com.example.orderjobabom.store.domain.StoreRepository;
import com.example.orderjobabom.store.presentation.dto.CategoryDto;
import com.example.orderjobabom.store.presentation.dto.StoreRequest;
import com.example.orderjobabom.user.test.MockUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

import static java.time.DayOfWeek.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*; // ★ jsonPath, status

@SpringBootTest
@AutoConfigureMockMvc
class StoreControllerTest {

    @Autowired ObjectMapper om;
    @Autowired StoreCreateService createService;
    @Autowired MockMvc mockMvc;
    @Autowired StoreRepository storeRepository;
    @Autowired StoreDeleteService storeDeleteService;

    StoreRequest request;

    @BeforeEach
    void init() {
        request = StoreRequest.builder()
                .storeName("테스트 매장")
                .storeAddress("인천광역시 계양구 임학안로 28번길 10")
                .storeTel("02-100-1000")
                .startHour(LocalTime.of(10, 0))
                .endHour(LocalTime.of(19, 0))
                .weekdays(List.of(MONDAY, TUESDAY, WEDNESDAY))
                .category(List.of(
                        new CategoryDto(Category.KOREAN, true),
                        new CategoryDto(Category.CHINESE, true)
                ))
                .build();
    }

    @Test
    @DisplayName("매장 생성 테스트 - CustomResponse<StoreCreateDto> 검증")
    @MockUser(roles = "OWNER")
    void createStoreTest() throws Exception {
        String body = om.writeValueAsString(request);

        mockMvc.perform(post("/v1/owner/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                // 상태 코드 (201 Created)
                .andExpect(status().isCreated())
                // CustomResponse 공통 필드
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("201"))
                .andExpect(jsonPath("$.message").exists())
                // payload: StoreCreateDto(storeId)
                // StoreId가 VO라면 보통 {"id":"<uuid>"} 형태일 가능성이 높음
                .andExpect(jsonPath("$.result.storeId").exists())
                .andExpect(jsonPath("$.result.storeId.id").exists());
    }

    @Test
    @DisplayName("매장 수정 테스트")
    @MockUser(roles="OWNER")
    @Transactional
    void updateStoreTest() throws Exception {
        StoreId storeId = createService.create(request).storeId();

        StoreRequest data = StoreRequest.builder()
                .storeName("(수정)테스트 매장")
                .storeAddress("(수정)인천광역시 계양구 임학안로 28번길 10")
                .storeTel("031-100-1000")
                .startHour(LocalTime.of(11, 0))
                .endHour(LocalTime.of(23, 0))
                .weekdays(List.of(MONDAY, WEDNESDAY))
                .category(List.of(
                        new CategoryDto(Category.KOREAN, true),
                        new CategoryDto(Category.ITALIAN, true)
                ))
                .build();

        String body = om.writeValueAsString(data);

        // 컨트롤러가 아직 void 반환이면 상태코드만 검증
        mockMvc.perform(patch("/v1/owner/stores/" + storeId.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print());
//                .andExpect(status().isOk());

        Store store = storeRepository.findById(storeId).orElseThrow();
    }

    @Test
    @DisplayName("매장 삭제 테스트")
    @MockUser(roles = "OWNER")
    void deleteStoreTest() throws Exception {
        StoreId storeId = createService.create(request).storeId();

        mockMvc.perform(delete("/v1/owner/stores/" + storeId.getId()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}