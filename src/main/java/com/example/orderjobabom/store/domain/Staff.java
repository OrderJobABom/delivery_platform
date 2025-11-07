package com.example.orderjobabom.store.domain;

import com.example.orderjobabom.user.domain.UserId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;

import java.util.Objects;

@ToString
@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Staff {

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "staff_id"))
    private UserId id;


    @Override
    public boolean equals(Object o){
        if ( o == null || getClass() != o.getClass()) return false;
        Staff staff = (Staff) o;
        return Objects.equals(id.getId(), staff.id.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id.getId());
    }
}
