package com.example.orderjobabom.user.application.service;

import com.example.orderjobabom.user.application.dto.TokenInfo;

public interface TokenGenerateService {
    TokenInfo generate(String username, String password);
}
