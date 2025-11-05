package com.example.orderjobabom.store.domain;

import java.util.Collection;

public interface OwnerRoleCheck {
    boolean check(Store store, Collection<Staff> staffs);
}
