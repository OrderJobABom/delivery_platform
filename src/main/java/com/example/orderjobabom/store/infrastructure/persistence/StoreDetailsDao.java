package com.example.orderjobabom.store.infrastructure.persistence;

import com.example.orderjobabom.store.domain.QStore;
import com.example.orderjobabom.store.domain.Store;
import com.example.orderjobabom.store.domain.StoreDetailsRepository;
import com.example.orderjobabom.store.domain.StoreId;
import com.example.orderjobabom.store.domain.dto.StoreSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StoreDetailsDao implements StoreDetailsRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Store findById(StoreId storeId) {
        QStore store = QStore.store;

        return queryFactory.selectFrom(store)
                .where(store.id.eq(storeId))
                .fetchFirst();
    }

    @Override
    public List<Store> findAll(StoreSearch search, int page, int size) {
        return List.of();
    }
}