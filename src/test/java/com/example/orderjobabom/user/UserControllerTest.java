package com.example.orderjobabom.user;

import com.example.orderjobabom.user.application.dto.UserRegister;
import com.example.orderjobabom.user.application.service.UserRegisterService;
import com.example.orderjobabom.user.presentation.dto.UserRegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 실제 서비스 대신 mock으로 교체
    @MockBean
    private UserRegisterService userRegisterService;

    @Test
    @DisplayName("회원가입 성공 테스트 - 실제 Keycloak, DB 없이")
    void register_success() throws Exception {
        // given
        UserRegisterRequest request = new UserRegisterRequest(
                "testuser",              // username
                "Test1234!",             // password
                "Test1234!",             // confirmPassword
                "test@test.com",         // email
                "길동",                  // firstName
                "홍",                    // lastName
                "01012345678"            // phone
        );

        // 서비스 호출 시 아무 동작도 하지 않도록 mock 처리
        Mockito.doNothing().when(userRegisterService).register(any(UserRegister.class));

        String json = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(
                post("/v1/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("회원가입이 완료되었습니다."));

        // register()가 1번 호출됐는지 검증
        Mockito.verify(userRegisterService, Mockito.times(1)).register(any(UserRegister.class));
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - 비밀번호 불일치")
    void register_fail_password_mismatch() throws Exception {
        // given
        UserRegisterRequest request = new UserRegisterRequest(
                "testuser",
                "Test1234!",
                "WrongPass!",
                "test@test.com",
                "길동",
                "홍",
                "01012345678"
        );

        String json = objectMapper.writeValueAsString(request);

        // when
        ResultActions result = mockMvc.perform(
                post("/v1/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        );

        // then
        result.andDo(print())
                .andExpect(status().isBadRequest()); // Validator에서 BadRequestException 발생 예상

        // register()는 호출되지 않아야 함
        Mockito.verify(userRegisterService, Mockito.never()).register(any());
    }
}
