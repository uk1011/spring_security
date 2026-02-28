package com.spring.springsecurity.repository;

import com.spring.springsecurity.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    @Query( value = """
            select p.* from permissions as p
                    join role_permissions as rp
                    on p.permission_id = rp.permission_id
                where rp.role_id = :roleId
        """, nativeQuery = true)
    List<Permission> getPermissionsByRole(Integer roleId);

}
