package com.example.orderjobabom.store.domain;

//import com.example.orderjobabom.global.infrastructure.persistence.BaseUserEntity;
import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Entity
@Table(name = "p_store")
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class Store extends BaseUserEntity {
public class Store{

    @EmbeddedId
    private StoreId id;

    @Embedded
    private OwnerSnapshot owner;

    @Embedded
    private StoreAddress address;

    @Embedded
    private PhoneNumber phone;

    @Embedded
    private GeoLocation location;

    @Embedded
    private OperatingHours hours;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false, length = 20)
    private String businessNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Category category;

    @Column(nullable = false)
    private Double rate; // 0.0 ~ 5.0

    @Builder
    private Store(StoreId id,
                  OwnerSnapshot owner,
                  StoreAddress address,
                  PhoneNumber phone,
                  GeoLocation location,
                  OperatingHours hours,
                  String name,
                  String businessNumber,
                  Category category,
                  Double rate) {

        this.id = (id == null ? StoreId.of() : id);

        if (owner == null) throw new IllegalArgumentException("owner required");
        if (address == null) throw new IllegalArgumentException("address required");
        if (phone == null) throw new IllegalArgumentException("phone required");
        if (location == null) throw new IllegalArgumentException("location required");
        if (hours == null) throw new IllegalArgumentException("hours required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name required");
        if (businessNumber == null || businessNumber.isBlank()) throw new IllegalArgumentException("biz no required");
        if (category == null) throw new IllegalArgumentException("category required");
        if (rate == null || rate < 0 || rate > 5) throw new IllegalArgumentException("rate 0..5");

        this.owner = owner;
        this.address = address;
        this.phone = phone;
        this.location = location;
        this.hours = hours;
        this.name = name;
        this.businessNumber = businessNumber;
        this.category = category;
        this.rate = rate;
    }

    @PrePersist
    private void prePersist() {
        if (this.id == null) this.id = StoreId.of();
    }

    // === 도메인 행위 ===
    public void rename(String newName) {
        if (newName == null || newName.isBlank()) throw new IllegalArgumentException("name required");
        this.name = newName;
    }

    public void changeAddress(StoreAddress newAddress) {
        if (newAddress == null) throw new IllegalArgumentException("address required");
        this.address = newAddress;
    }

    public void changePhone(PhoneNumber newPhone) {
        if (newPhone == null) throw new IllegalArgumentException("phone required");
        this.phone = newPhone;
    }

    public void relocate(GeoLocation newLocation) {
        if (newLocation == null) throw new IllegalArgumentException("location required");
        this.location = newLocation;
    }

    public void changeHours(OperatingHours newHours) {
        if (newHours == null) throw new IllegalArgumentException("hours required");
        this.hours = newHours;
    }

    public void changeCategory(Category newCategory) {
        if (newCategory == null) throw new IllegalArgumentException("category required");
        this.category = newCategory;
    }

    public void changeRate(Double newRate) {
        if (newRate == null || newRate < 0 || newRate > 5) throw new IllegalArgumentException("rate 0..5");
        this.rate = newRate;
    }

    // Soft delete: BaseEntity.deletedAt 사용
//    public void softDelete() {
//        if (this.deletedAt != null) return;
//        this.deletedAt = java.time.LocalDateTime.now();
//    }
//
//    public boolean isDeleted() {
//        return this.deletedAt != null;
//    }
}
