//package com.example.orderjobabom.store.application.service;
//
//import com.example.orderjobabom.store.domain.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.UUID;
//
//
//@Service
//@RequiredArgsConstructor
//public class StoreDeleteService {
//
//    private final StoreDetailsRepository detailsRepository;
//    private final StoreRepository repository;
//    private final RoleCheck roleCheck;
//
//    @Transactional
//    public void delete(UUID id) {
//        StoreId storeId = StoreId.of(id);
//        Store.exists(storeId, repository); // 상점이 등록되어 있는지 체크
//
//        Store store = detailsRepository.findById(storeId);
//        store.isEditable(roleCheck);
//
//        store.delete();
//    }
//}