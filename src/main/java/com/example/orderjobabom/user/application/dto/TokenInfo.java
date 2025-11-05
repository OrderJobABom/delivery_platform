package com.example.orderjobabom.user.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenInfo(
        @JsonProperty("access_token") String access_token,
        @JsonProperty("expires_in") int expires_in,
        @JsonProperty("refresh_expires_in") int refresh_expires_in,
        @JsonProperty("refresh_token") String refresh_token,
        @JsonProperty("token_type") String token_type
) {}
