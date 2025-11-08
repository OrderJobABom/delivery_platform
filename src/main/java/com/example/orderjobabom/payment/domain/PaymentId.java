package com.example.orderjobabom.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class PaymentId {

    @Column(name = "payment_id", columnDefinition = "uuid")
    private UUID id;

    private PaymentId(UUID id) {
        this.id = id;
    }

    /** 새 PaymentId 생성 */
    public static PaymentId create() {
        return new PaymentId(UUID.randomUUID());
    }

    /** 특정 UUID로 PaymentId 생성 */
    public static PaymentId from(UUID id) {
        return new PaymentId(id);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
