package com.example.orderjobabom.store.infrastructure;

import com.example.orderjobabom.store.domain.OwnerRoleCheck;
import com.example.orderjobabom.store.domain.Staff;
import com.example.orderjobabom.store.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class SecurityOwnerRoleCheck implements OwnerRoleCheck {
    @Override
    public boolean check(Store store, Collection<Staff> staffs){

        // TODO: 직원은 모두 직원 권한 (ROLE_STAFF) 체크 필요

        //가게 사장일때 추가 / 제거 가능
        return SecurityRoleCheckHelper.getRole(store).equals("OWNER");
    }
}
