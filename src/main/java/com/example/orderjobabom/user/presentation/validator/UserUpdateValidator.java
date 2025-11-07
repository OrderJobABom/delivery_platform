package com.example.orderjobabom.user.presentation.validator;

import com.example.orderjobabom.user.presentation.dto.PasswordChangeRequest;
import com.example.orderjobabom.user.presentation.dto.UserUpdateRequest;
import org.springframework.util.StringUtils;

public class UserUpdateValidator {

    public void validateUpdateProfile(UserUpdateRequest req) {
        if (!StringUtils.hasText(req.email()) &&
                !StringUtils.hasText(req.firstName()) &&
                !StringUtils.hasText(req.lastName()) &&
                !StringUtils.hasText(req.phone())) {
            throw new IllegalArgumentException("수정할 항목이 없습니다.");
        }
    }

    public void validateChangePassword(PasswordChangeRequest req) {
        if (!StringUtils.hasText(req.password())) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }

        if (req.password().length() < 8) {
            throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
        }
    }
}
