package com.example.orderjobabom.store.application.service;

import com.example.orderjobabom.store.domain.RoleCheck;
import com.example.orderjobabom.store.domain.Store;
import com.example.orderjobabom.store.domain.StoreDetailsRepository;
import com.example.orderjobabom.store.domain.StoreId;
import com.example.orderjobabom.store.domain.service.StoreAddressService;
import com.example.orderjobabom.store.domain.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreUpdateService {

    private final RoleCheck roleCheck;
    private final StoreDetailsRepository detailsRepository;
    private final StoreRepository repository;
    private final StoreAddressService addressService;

    // 상점 일반 정보 수정
    public void updateInfo(UUID id, String storeName, String storeTel){
        Store store = validateAndGet(id);;

        store.changeInfo(storeName, storeTel);

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
