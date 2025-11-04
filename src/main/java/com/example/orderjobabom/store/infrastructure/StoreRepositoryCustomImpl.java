package com.example.orderjobabom.store.infrastructure;

import com.example.orderjobabom.store.domain.Store;
import com.example.orderjobabom.store.domain.StoreId;
import com.example.orderjobabom.store.repository.StoreRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;

import java.util.*;

@RequiredArgsConstructor
public class StoreRepositoryCustomImpl implements StoreRepositoryCustom {

    private final JpaQueryMethodFactory queryFactory;

    @Override
    public Optional<Store> findByIdNotDeleted(StoreId id) {
        QStore s = QStore.store;
        Store result = queryFactory.selectFrom(s)
                .where(s.id.eq(id), s.deletedAt.isNull())
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Store> findByOwnerId(UUID ownerId) {
        QStore s = QStore.store;
        Store result = queryFactory.selectFrom(s)
                .where(s.owner.id.id.eq(ownerId), s.deletedAt.isNull())
                .limit(1)
                .fetchFirst();
        return Optional.ofNullable(result);
    }

    @Override
    public boolean existsByOwnerId(UUID ownerId) {
        QStore s = QStore.store;
        StoreId first = queryFactory.select(s.id)
                .from(s)
                .where(s.owner.id.id.eq(ownerId), s.deletedAt.isNull())
                .limit(1)
                .fetchFirst();
        return first != null;
    }

    @Override
    public Page<Store> findAllNotDeleted(Pageable pageable) {
        QStore s = QStore.store;

        Long total = queryFactory.select(s.count())
                .from(s)
                .where(s.deletedAt.isNull())
                .fetchOne();

        var content = queryFactory.selectFrom(s)
                .where(s.deletedAt.isNull())
                .orderBy(s.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    @Override
    public Page<Store> findAllIncludingDeleted(Pageable pageable) {
        QStore s = QStore.store;

        Long total = queryFactory.select(s.count())
                .from(s)
                .fetchOne();

        var content = queryFactory.selectFrom(s)
                .orderBy(s.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }
}
