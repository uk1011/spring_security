package com.spring.springsecurity.entity.compositekeys;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.IdClass;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Objects;
@EqualsAndHashCode
public class RolePermissionKey implements Serializable {
    private Integer  role;
    private Integer permission;

    public RolePermissionKey() {
    }


}
