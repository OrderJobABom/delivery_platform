package com.example.orderjobabom.store.infrastructure;

import com.example.orderjobabom.store.domain.RoleCheck;
import com.example.orderjobabom.store.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class SecurityRoleCheck implements RoleCheck {
    @Override
    public boolean check(Store store){
        if(store == null) return false;

        String role = SecurityRoleCheckHelper.getRole(store);

        return StringUtils.hasText(role);
    }
}
