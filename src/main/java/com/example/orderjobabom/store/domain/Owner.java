package com.example.orderjobabom.store.domain;

import com.example.orderjobabom.user.domain.UserId;
import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Owner {

    @Embedded
    @AttributeOverrides(
            @AttributeOverride(name="id", column = @Column(name="owner_id"))
    )
    public UserId id;

    @Column(name="owner_name")
    public String name;
}
