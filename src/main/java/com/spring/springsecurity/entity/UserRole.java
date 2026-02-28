package com.spring.springsecurity.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.spring.springsecurity.entity.compositekeys.UserRoleKey;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Table(name = "user_roles")
@Entity
@Getter @Setter
@IdClass(UserRoleKey.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Id
    @JsonBackReference("user-userRole")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    @Id
    private Role role;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;
    @Column(nullable = false, updatable = false)
    private Integer createdBy;

    @UpdateTimestamp
    private Timestamp updatedAt;
    private Integer updatedBy;

}
