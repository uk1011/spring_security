package com.lenox.lenox_api.repository;

import com.lenox.lenox_api.entity.Role;
import com.lenox.lenox_api.entity.User;
import com.lenox.lenox_api.entity.UserRole;
import com.lenox.lenox_api.entity.compositekeys.UserRoleKey;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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
