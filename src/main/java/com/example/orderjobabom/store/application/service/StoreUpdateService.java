package com.example.orderjobabom.store.application.service;

import com.example.orderjobabom.store.domain.*;
import com.example.orderjobabom.store.domain.service.StoreAddressService;
import com.example.orderjobabom.store.presentation.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreUpdateService {

    private final RoleCheck roleCheck;
    private final StoreDetailsRepository detailsRepository;
    private final StoreRepository repository;
    private final StoreAddressService addressService;

    // 상점 일반 정보 수정
    public void updateInfo(UUID id, String storeName, String storeTel, List<CategoryDto> categories) {
        Store store = validateAndGet(id);;

        store.changeInfo(storeName, storeTel);

        store.emptyCategory();
        categories.forEach(c -> store.addCategory(c.category(), c.active()));
        repository.save(store);
    }

    // 상점 운영 정보 수정
    public void updateOperatingInfo(UUID id, LocalTime startTime, LocalTime endTime, List<DayOfWeek> weekdays){
        Store store = validateAndGet(id);
        store.changeOperatingInfo(startTime, endTime, weekdays);
    }

    // 상점 주소 정보 수정
    public void updateAddressInfo(UUID id, String address){
        Store store = validateAndGet(id);
        store.changeAddress(address, addressService);
    }

    private Store validateAndGet(UUID id){
        StoreId storeId = StoreId.of(id);
        Store.exists(storeId, repository);

        Store store = detailsRepository.findById(storeId);
        store.isEditable(roleCheck);

        return store;
    }
}
