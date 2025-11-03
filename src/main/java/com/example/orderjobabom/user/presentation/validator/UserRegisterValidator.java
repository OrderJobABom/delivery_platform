package com.example.orderjobabom.user.presentation.validator;

import com.example.orderjobabom.global.presentation.exception.BadRequestException;
import com.example.orderjobabom.user.presentation.dto.UserRegisterRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 회원가입 시 추가 검증 로직
 */
@Component
public class UserRegisterValidator implements PasswordValidator, MobileValidator {

    public void validate(UserRegisterRequest req) {

        String password = req.password();
        String confirmPassword = req.confirmPassword();

        // 비밀번호 복잡성 체크
        if (!checkAlpha(password, false) || !checkNumber(password) || !checkSpecialChars(password)) {
            throw new BadRequestException("비밀번호는 알파벳 대소문자, 숫자, 특수 문자를 포함해야 합니다.");
        }

        // 비밀번호 일치 여부
        if (!password.equals(confirmPassword)) {
            throw new BadRequestException("비밀번호가 일치하지 않습니다.");
        }

        // 휴대전화 번호 형식 체크
        String phone = req.phone();
        if (StringUtils.hasText(phone) && !checkMobile(phone)) {
            throw new BadRequestException("휴대전화 번호 형식이 올바르지 않습니다.");
        }
    }
}
