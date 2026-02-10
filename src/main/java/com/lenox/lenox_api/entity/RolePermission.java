package com.lenox.lenox_api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lenox.lenox_api.entity.compositekeys.RolePermissionKey;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Set;

@Table(name = "role_permissions")
@Entity
@Getter @Setter
@IdClass(RolePermissionKey.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermission {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @Id
    @JsonBackReference("role-rolePermission")
    private Role role;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    @JsonManagedReference("permission-rolePermission")
    private Permission permission;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;
    @Column(nullable = false, updatable = false)
    private Integer createdBy;

    @UpdateTimestamp
    private Timestamp updatedAt;
    private Integer updatedBy;

}
