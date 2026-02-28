package com.spring.springsecurity.repository;

import com.spring.springsecurity.entity.Permission;
import com.spring.springsecurity.entity.RolePermission;
import com.spring.springsecurity.entity.compositekeys.RolePermissionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionKey> {

    @Query(value = """
            select p.* 
                from permissions p 
                join role_permissions rp
                on p.permission_id = rp.permission_id
                where rp.role_id = :roleId
            """, nativeQuery = true)
    List<Permission> findAllByRoleId(@Param("roleId") Integer roleId );

    @Query(value = """
            select * from role_permissions
                        where ((role_id = :roleId) and (permission_id = :permissionId))
            """, nativeQuery = true)
    RolePermission findByRoleIdAndPermissionId(@Param("roleId") Integer roleId,@Param("permissionId") Integer permissionId);

    public List<RolePermission> findAll();

}
