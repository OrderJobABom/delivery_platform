package com.example.orderjobabom.payment.infrastructure.api;


import com.example.orderjobabom.payment.infrastructure.dto.TossPaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TossPaymentClient {

    @Value("${toss.secret-key}")
    private String secretKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://api.tosspayments.com/v1/payments";

    private HttpHeaders headers() {
        String encoded = Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encoded);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public TossPaymentResponse confirmPayment(String paymentKey, String orderId, int amount) {
        Map<String, Object> body = Map.of(
                "paymentKey", paymentKey,
                "orderId", orderId,
                "amount", amount
        );
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers());
        ResponseEntity<TossPaymentResponse> response =
                restTemplate.postForEntity(BASE_URL + "/confirm", entity, TossPaymentResponse.class);
        return response.getBody();
    }

    public TossPaymentResponse cancelPayment(String paymentKey, String cancelReason) {
        String url = BASE_URL + "/" + paymentKey + "/cancel";

        Map<String, Object> body = new HashMap<>();
        body.put("cancelReason", cancelReason);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers());

        try {
            ResponseEntity<TossPaymentResponse> response =
                    restTemplate.postForEntity(url, entity, TossPaymentResponse.class);

            if (response.getStatusCode() != HttpStatus.OK) {
                throw new IllegalStateException("Toss 환불 요청 실패: " + response.getStatusCode());
            }

            return response.getBody();

        } catch (RestClientException e) {
            throw new IllegalStateException("Toss API 호출 중 오류 발생: " + e.getMessage(), e);
        }
    }
}