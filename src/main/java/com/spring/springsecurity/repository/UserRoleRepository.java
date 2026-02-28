package com.spring.springsecurity.repository;

import com.spring.springsecurity.entity.Role;
import com.spring.springsecurity.entity.UserRole;
import com.spring.springsecurity.entity.compositekeys.UserRoleKey;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleKey> {

    @Query(value = """
                select r.* from roles r 
                            join user_roles ur 
                            on r.role_id = ur.role_id
                            where ur.user_id = :userId
            """, nativeQuery = true)
    public List<Role> findAllRolesByUserId(@Param("userId") Integer userId);

    @Query(value = """
                select * from user_roles
                            where user_id = :userId
            """, nativeQuery = true)
    public List<UserRole> findByUserId(@Param("userId") Integer userid);

    @Query(value = """
                select * from user_roles
                            where ((user_id = :userId) and (role_id) = :roleId)
            """, nativeQuery = true)
    public UserRole findByUserIdAndRoleId(@Param("userId") Integer userId,@Param("roleId") Integer roleId);

    @EntityGraph(attributePaths = {
            "role",
            "user"
    })
    public List<UserRole> findAll();

}
