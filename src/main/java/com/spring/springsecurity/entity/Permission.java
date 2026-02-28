package com.spring.springsecurity.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "permissions_seq")
    @SequenceGenerator(
            name = "permissions_seq",
            sequenceName = "permission_id_auto_generate",
            allocationSize = 1
    )
    private Integer permissionId;

    private String permissionName;

    private String scope;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;
    @Column(nullable = false, updatable = false)
    private Integer createdBy;

    @UpdateTimestamp
    private Timestamp updatedAt;
    private Integer updatedBy;

    @OneToMany(mappedBy = "permission", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonBackReference("permission-rolePermission")
    private Set<RolePermission> rolesPermissions;

}
