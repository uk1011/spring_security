package com.lenox.lenox_api.repository;


import com.lenox.lenox_api.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @EntityGraph(attributePaths = {
            "userRoleList",
            "userRoleList.role",
            "userRoleList.role.rolePermissions",
            "userRoleList.role.rolePermissions.permission"
    })
    User findByfirstName(String username);
    @EntityGraph(attributePaths = {
            "userRoleList",
            "userRoleList.role",
            "userRoleList.role.rolePermissions",
            "userRoleList.role.rolePermissions.permission"
    })
    Optional<User> findById(Integer id);

    @EntityGraph(attributePaths = {
            "userRoleList",
            "userRoleList.role",
            "userRoleList.role.rolePermissions",
            "userRoleList.role.rolePermissions.permission"
    })
    List<User> findAll();
}
