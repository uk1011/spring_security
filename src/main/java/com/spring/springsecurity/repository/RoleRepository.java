package com.spring.springsecurity.repository;

import com.spring.springsecurity.entity.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(value = """
        select r.* from roles r 
            join user_roles as ur 
            on r.role_id = ur.role_id
            where ur.user_id = :userId
    """, nativeQuery = true)
    public List<Role> findAllRolesForUser(Integer userId);

    @EntityGraph(attributePaths = {
            "rolePermissions",
            "rolePermissions.permission"
    })
    public List<Role> findAll();
}
