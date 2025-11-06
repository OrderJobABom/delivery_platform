package com.example.orderjobabom.store.application.service;

import com.example.orderjobabom.store.domain.Store;
import com.example.orderjobabom.store.domain.StoreCategory;
import com.example.orderjobabom.store.domain.StoreId;
import com.example.orderjobabom.store.domain.StoreRepository;
import com.example.orderjobabom.store.presentation.dto.StoreRequest;
import com.example.orderjobabom.user.domain.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreCreateService {

    private  final StoreRepository repository;

    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER', 'MASTER')")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public StoreId create(StoreRequest req){
        // 회원정보
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID userId = UUID.fromString(jwt.getSubject());
        String name = jwt.getClaim("family_name") + (String) jwt.getClaim("given_name");

        LocalTime startHour = req.startHour();
        LocalTime endHour = req.endHour();
        if (startHour != null && endHour != null && endHour.isBefore(startHour)) {
            LocalTime tmp = endHour;
            endHour = startHour;
            startHour = tmp;
        }
        Store store = Store.builder()
                .storeName(req.storeName())
                .storeTel(req.storeTel())
                .address(req.storeAddress())
                .categories(req.category() == null ? null : req.category().stream().map(c -> new StoreCategory(c.category(), c.active())).toList())
                .startHour(startHour)
                .endHour(endHour)
                .weekdays(req.weekdays())
                .userId(UserId.of(userId))
                .userName(name)
                .build();

        repository.save(store);

        return store.getId();
    }
}
