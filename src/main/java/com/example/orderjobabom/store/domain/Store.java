package com.example.orderjobabom.store.domain;

//import com.example.orderjobabom.global.infrastructure.persistence.BaseUserEntity;
import com.example.orderjobabom.store.domain.service.StoreAddressService;
import com.example.orderjobabom.store.domain.exception.CategoryNotFoundException;
import com.example.orderjobabom.store.domain.exception.StaffNotEditableException;
import com.example.orderjobabom.store.domain.exception.StoreNotEditableException;
import com.example.orderjobabom.store.domain.exception.StoreNotFoundException;
import com.example.orderjobabom.store.infrastructure.persistence.converter.StaffConverter;
import com.example.orderjobabom.store.domain.StoreRepository;
import com.example.orderjobabom.user.domain.UserId;
import com.example.orderjobabom.global.infrastructure.persistence.Price;
import com.example.orderjobabom.global.presentation.exception.FailException;
import com.example.orderjobabom.menu.domain.*;
import com.example.orderjobabom.menu.presentation.dto.request.UpdateItemRequestDTO;
import com.example.orderjobabom.store.domain.exception.StoreErrorCode;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.StringUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.List;

@Getter
@Entity
@Access(AccessType.FIELD)
@Table(name = "P_STORE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {

    @EmbeddedId
    private StoreId id;

    @Embedded
    private Owner owner;

    @Convert(converter = StaffConverter.class)
    private Set<Staff> staffs; // 직원들

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="P_STORE_CATEGORY", joinColumns = @JoinColumn(name="store_id"))
    @OrderColumn(name="category_idx")
    private List<StoreCategory> categories;

    @Column(length=100, nullable = false)
    private String storeName;

    @Column(length=45, nullable = false)
    private String storeTel;

    @Embedded
    private StoreAddress address;

    @Embedded
    private OperatingInfo operatingInfo;

    @Builder
    public Store(StoreId id, String storeName, String storeTel, String address, LocalTime startHour, LocalTime endHour, List<DayOfWeek> weekdays, List<StoreCategory> categories, UserId userId, String userName, StoreAddressService addressService) {
        this.id = Objects.requireNonNullElse(id, StoreId.of());
        this.storeName = storeName;
        this.storeTel = storeTel;
        this.address = StoreAddress.of(address);
        this.operatingInfo = new OperatingInfo(startHour, endHour, weekdays);
        this.owner = new Owner(userId, userName);
        setCategories(categories);
    }

    private void setCategories(List<StoreCategory> categories) {
        if (categories == null || categories.isEmpty()) return;

        this.categories = categories.stream().distinct().toList();
    }


    public void delete() {
//        deletedAt = LocalDateTime.now();
    }

    /**
     *  삭제, 수정 권한
     */
    public void isEditable(RoleCheck roleCheck) {
        if (!roleCheck.check(this)) {
            // 권한이 없는 경우
            throw new StoreNotEditableException();
        }
    }

    public void addCategory(Category category, boolean active) {
        categories = Objects.requireNonNullElseGet(categories, ArrayList::new);
        categories.add(new StoreCategory(category, active));
        categories = categories.stream().distinct().toList();
    }

    // Item 생성
    public Item createItem(Category category, Price price, String name, ItemStatus itemStatus, Stock stock, List<ItemOption> itemOptions) {

    public void removeCategory(Category category) {
        removeCategory(List.of(category));
    }
        if (category != null && !categoryExists(category)) {
            throw new FailException(StoreErrorCode.CATEGORY_NOT_FOUND);
        }

    public void removeCategory(List<Category> categories) {
        if (this.categories == null || categories.isEmpty()) return;
        return Item.builder()
                .storeId(id)
                .category(category)
                .status(itemStatus)
                .price(price)
                .name(name)
                .stock(stock)
                .itemOptions(itemOptions)
                .build();

        this.categories = this.categories.stream().filter(c -> !categories.contains(c.getCategory())).toList();
    }

    public boolean categoryExists(Category category) {
        return categories != null && categories.stream().anyMatch(c -> c.getCategory() == category);
    }



    /**
     * 직원 추가
     *  OWNER권한만 추가가능, 가능한 회원은 STAFF 권한이 있어야 한다.
     * @param staffs
     */
    public void addStaff(Collection<Staff> staffs, OwnerRoleCheck roleCheck) {
        if (!roleCheck.check(this, staffs)) {
            throw new StaffNotEditableException();
        }

        this.staffs = Objects.requireNonNullElseGet(this.staffs, HashSet::new);
        this.staffs.addAll(staffs);
    }

    public void addStaff(Staff staff, OwnerRoleCheck roleCheck) {
        addStaff(List.of(staff), roleCheck);
    }

    /**
     * 직원 제거
     *
     * @param staffs
     */
    public void removeStaff(Collection<Staff> staffs, OwnerRoleCheck roleCheck) {
        if (!roleCheck.check(this, staffs)) {
            throw new StaffNotEditableException();
        }
        return category != null && categories.stream().anyMatch(ca -> ca.getCategory() == category);

        this.staffs.removeAll(staffs);
    }

    public void removeStaff(Staff staff, OwnerRoleCheck roleCheck) {
        removeStaff(List.of(staff), roleCheck);
    }
    public void updateItem(UpdateItemRequestDTO dto) {


    public static void exists(StoreId id, StoreRepository repository) {
        if (!repository.existsById(id)) {
            throw new StoreNotFoundException();
        }
    }

    /**
     * 매장 일반 정보 수정
     *
     * @param storeName
     * @param storeTel
     */
    public void changeInfo(String storeName, String storeTel) {
        this.storeName = storeName;
        this.storeTel = storeTel;
    }

    /**
     * 매장 주소 변경, 위도 경도 정보도 함께 업데이트
     * @param address
     */
    public void changeAddress(String address, StoreAddressService service) {
        if (!StringUtils.hasText(address) || service == null) return;
        List<Double> coords = service.getCoordinate(address);
        this.address = new StoreAddress(address, coords.get(0), coords.get(1));
    }

    /**
     * 매장 운영시간, 운영 요일
     *
     * @param startHour
     * @param endHour
     * @param weekdays
     */
    public void changeOperatingInfo(LocalTime startHour, LocalTime endHour, List<DayOfWeek> weekdays) {
        // 등록이 가능한지 여부 체크
        if (startHour != null && endHour != null && endHour.isBefore(startHour)) {
            LocalTime tmp = endHour;
            endHour = startHour;
            startHour = tmp;
        }

        this.operatingInfo = new OperatingInfo(startHour, endHour, weekdays);
    }
}