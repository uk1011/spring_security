package com.spring.springsecurity.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "users_seq")
    @SequenceGenerator(
            name = "users_seq",
            sequenceName = "user_id_auto_generate",
            allocationSize = 1
    )
    private Integer userId;

    @Column(nullable = false, unique = true)
    private String firstName;

    private String lastName;

    @Column(nullable = false)
    private String hashedPassword;

    @Column(nullable = false)
    private Boolean isPasswordChangeRequired = true;

    @Column(nullable = false, unique = true)
    @NonNull
    @Digits(fraction = 0, integer = 13, message = "Phone number must contain only digits")
    private BigInteger phoneNumber;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;
    @Column(nullable = false, updatable = false)
    private Integer createdBy;

    @UpdateTimestamp
    private Timestamp updatedAt;
    private Integer updatedBy;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user", orphanRemoval = true)
    @JsonManagedReference("user-userRole")
    private Set<UserRole> userRoleList;


}
