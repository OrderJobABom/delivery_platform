//package com.example.orderjobabom.store.infrastructure.persistence;
//
//import com.example.orderjobabom.store.domain.Category;
//import com.example.orderjobabom.store.domain.Store;
//import com.example.orderjobabom.store.domain.StoreDetailsRepository;
//import com.example.orderjobabom.store.domain.StoreId;
//import com.example.orderjobabom.store.domain.dto.StoreSearch;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import org.springframework.util.StringUtils;
//
//import java.util.List;
//
//@Repository
//@RequiredArgsConstructor
//public class StoreDetailsDao implements StoreDetailsRepository {
//
//    private final JPAQueryFactory queryFactory;
//
//    /**
//     * 상점 하나 조회
//     *
//     * @param storeId
//     * @return
//     */
//    @Override
//    public Store findById(StoreId storeId) {
//        QStore store = QStore.store;
//        return queryFactory.selectFrom(store)
//                .where(store.id.eq(storeId))
//                .fetchFirst();
//    }
//
//    /**
//     *
//     * @param search
//     * @param page, 1페이지가 시작 페이지 기준
//     * @param size
//     * @return
//     */
//    @Override
//    public List<Store> findAll(StoreSearch search, int page, int size) {
//        QStore store = QStore.store;
//
//        BooleanBuilder andBuilder = new BooleanBuilder();
//
//        // 상점 분류 검색 S
//        List<Category> category = search.category();
//        if (category != null && !category.isEmpty()) {
//            andBuilder.and(store.categories.any().category.in(category));
//        }
//        // 상점 분류 검색 E
//
//        // 상점명 검색
//        String storeName = search.storeName();
//        if (StringUtils.hasText(storeName)) {
//            andBuilder.and(store.storeName.contains(storeName.trim()));
//        }
//
//        // 상점 전화번호 검색
//        String storeTel = search.storeTel();
//        if (StringUtils.hasText(storeTel)) {
//            storeTel = storeTel.replaceAll("\\D",  "");
//            andBuilder.and(store.storeTel.contains(storeTel));
//        }
//
//        // 지역 검색, 시도 -> 시구군 -> 동
//        String sido = search.sido();
//        String sigugun = search.sigugun();
//        String dong = search.dong();
//        if (StringUtils.hasText(sido)) {
//            StringPath addressPath = store.address.address;
//            andBuilder.and(addressPath.startsWith(sido.trim()));
//            // 시구군
//            if (StringUtils.hasText(sigugun)) {
//                andBuilder.and(addressPath.contains(sigugun.trim()));
//
//                // 동이름
//                if (StringUtils.hasText(dong)) {
//                    andBuilder.and(addressPath.contains(dong.trim()));
//                }
//            }
//        }
//
//        // 키워드 검색 - 상점명 + 전화번호 + 주소
//        String keyword = search.keyword();
//        if (StringUtils.hasText(keyword)) {
//            keyword = keyword.trim();
//            andBuilder.and(store.storeName.concat(store.storeTel).concat(store.address.address).contains(keyword));
//        }
//
//        // 페이징 처리
//        page = Math.max(page, 1);
//        size = size < 1 ? 20 : size;
//        int offset = (page - 1) * size;
//
//        List<Store> items = queryFactory.selectFrom(store)
//                .where(andBuilder)
//                .offset(offset)
//                .limit(size)
//                .orderBy(store.createdAt.desc())
//                .fetch();
//
//
//        return List.of();
//    }
//}