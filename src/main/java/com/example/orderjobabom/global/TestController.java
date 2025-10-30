package com.example.orderjobabom.global;

import com.example.orderjobabom.global.presentation.CustomResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "test컨트롤러", description = "테스트용입니다.")
public class TestController {
    
    @GetMapping("/v1/user/test")
    public CustomResponse<?> testMethod() {

        return CustomResponse.onSuccess("테스트");
    }
}
