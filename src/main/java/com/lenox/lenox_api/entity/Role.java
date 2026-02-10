package com.lenox.lenox_api.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "roles_seq")
    @SequenceGenerator(
            name = "roles_seq",
            sequenceName = "role_id_auto_generate",
            allocationSize = 1
    )
    private Integer roleId;

    @Column(unique = true)
    private String roleName;

    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;
    @Column(nullable = false, updatable = false)
    private Integer createdBy;

    @UpdateTimestamp
    private Timestamp updatedAt;
    private Integer updatedBy;

    @OneToMany(mappedBy = "role", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<UserRole>  userRoles;

    @OneToMany(mappedBy = "role", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference("role-rolePermission")
    private Set<RolePermission> rolePermissions;

}
