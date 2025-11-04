package com.example.orderjobabom.store.repository;

import com.example.orderjobabom.store.domain.Store;
import com.example.orderjobabom.store.domain.StoreId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, StoreId>, StoreRepositoryCustom { }
