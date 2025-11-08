package com.example.orderjobabom.payment.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentTempService {

    private final StringRedisTemplate redisTemplate;

    /** Redis에 결제 금액 임시 저장 (10분 유효) */
    public void saveAmount(UUID orderId, int amount) {
        redisTemplate.opsForValue().set(
                orderId.toString(),
                String.valueOf(amount),
                Duration.ofMinutes(10)
        );
    }

    /** Redis에서 금액 조회 */
    public Integer getAmount(UUID orderId) {
        String value = redisTemplate.opsForValue().get(orderId.toString());
        return (value != null) ? Integer.parseInt(value) : null;
    }

    /** 결제 완료 후 삭제 */
    public void delete(UUID orderId) {
        redisTemplate.delete(orderId.toString());
    }
}