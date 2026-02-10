package com.lenox.lenox_api.entity.compositekeys;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Objects;

@EqualsAndHashCode
public class UserRoleKey implements Serializable {
    private Integer user;
    private Integer role;

    public UserRoleKey() {}

}
