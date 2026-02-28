package com.spring.springsecurity.service;

import com.spring.springsecurity.entity.Permission;
import com.spring.springsecurity.entity.Role;
import com.spring.springsecurity.entity.RolePermission;
import com.spring.springsecurity.globalexception.exceptions.NoPermissionFoundException;
import com.spring.springsecurity.globalexception.exceptions.NoRoleFoundException;
import com.spring.springsecurity.globalexception.exceptions.NoRolePermissionMappingFoundException;
import com.spring.springsecurity.globalexception.exceptions.RolePermissionAlreadyExistsException;
import com.spring.springsecurity.repository.RolePermissionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final RoleService roleService;
    private final PermissionService permissionService;

    public RolePermissionService(RolePermissionRepository rolePermissionRepository, RoleService roleService, PermissionService permissionService) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    public Map<String, Object> toRolePermissionResponse(RolePermission rolePermission) {
        LinkedHashMap<String, Object> rolePermissionResponse = new LinkedHashMap<>();
        rolePermissionResponse.put("roleId", rolePermission.getRole().getRoleId());
        rolePermissionResponse.put("roleName", rolePermission.getRole().getRoleName());
        List<Permission> permissions = rolePermissionRepository.findAllByRoleId(rolePermission.getRole().getRoleId());
        if(!permissions.isEmpty()) {
            rolePermissionResponse.put("permissions_associated", permissions);
        }
        rolePermissionResponse.put("created_at", rolePermission.getCreatedAt());
        rolePermissionResponse.put("created_by", rolePermission.getCreatedBy());
        rolePermissionResponse.put("updated_at", rolePermission.getUpdatedAt());
        rolePermissionResponse.put("updated_by", rolePermission.getUpdatedBy());
        return rolePermissionResponse;
    }

    public RolePermission toRolePermission(Integer roleId, Integer permissionId) {
        return RolePermission.builder()
                .role(roleService.getRoleById(roleId))
                .permission(permissionService.getPermissionById(permissionId))
                .build();
    }

    public List<Map<String, Object>> getRolePermissionsById(Integer roleId) {
        List<Permission> permissions = rolePermissionRepository.findAllByRoleId(roleId);
        if(permissions.isEmpty()) {
            throw new NoRolePermissionMappingFoundException("no mapping with permission found for this role during fetch of permissions for roles", HttpStatus.OK);
        }
        return permissions.stream()
                .map(permissionService::toPermissionResponse)
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getAllRolePermissions(){
        List<RolePermission> rolePermissions = rolePermissionRepository.findAll();
        if(rolePermissions.isEmpty()) {
            throw new NoRolePermissionMappingFoundException("no mapping with permission found for all roles", HttpStatus.OK);
        }
        return rolePermissions.stream()
                .map(this::toRolePermissionResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> createRolePermissions(Integer roleId, Integer permissionId) {
        RolePermission rolePermission = toRolePermission(roleId, permissionId);

            List<Permission> permissions = rolePermissionRepository.findAllByRoleId(roleId);
            if(permissions.isEmpty()) {
                throw new NoPermissionFoundException("no permission found associated with this role id", HttpStatus.OK);
            }
            for(Permission p : permissions) {
                if(p.getPermissionId().equals(permissionId)) {
                    throw new RolePermissionAlreadyExistsException("role permission already exists for this role id", HttpStatus.OK);
                }
            }
        RolePermission createdRolePermission = rolePermissionRepository.save(rolePermission);
        if (createdRolePermission != null) {
            Role role = roleService.getRoleById(roleId);
            if(role == null) {
                throw new NoRoleFoundException("no role found with this id during role permission creation", HttpStatus.OK);
            }
            role.getRolePermissions().add(createdRolePermission);
            Permission permission = permissionService.getPermissionById(permissionId);
            if(permission == null) {
                throw new NoPermissionFoundException("no permission found with this id during role permission creation", HttpStatus.OK);
            }
            permission.getRolesPermissions().add(createdRolePermission);
            return toRolePermissionResponse(createdRolePermission);
        }
        throw new RuntimeException("not able to create role permission");
    }

    @Transactional
    public Map<String, Object> updateRolesPermissions(Integer roleId,Integer permissionId){
        Permission permission = permissionService.getPermissionById(permissionId);
        if(permission == null){
            throw new NoPermissionFoundException("no permission found with this id during role permissions update", HttpStatus.OK);
        }
        Role role = roleService.getRoleById(roleId);
        if(role == null){
            throw new NoRoleFoundException("no role found with this id during role permissions update", HttpStatus.OK);
        }
        List<Permission> oldPermissions = rolePermissionRepository.findAllByRoleId(roleId);
        if(oldPermissions != null){
            for(Permission oldPermission : oldPermissions){
                if(oldPermission.getPermissionId().equals(permission.getPermissionId())){
                    throw new RolePermissionAlreadyExistsException("this role permission already exists in database during role permissions update", HttpStatus.ACCEPTED);
                }
            }
        }
        return createRolePermissions(roleId,permissionId);
    }

    @Transactional
    public Map<String, Object> deleteRolePermissions(Integer roleId, Integer permissionId){
        RolePermission deletedRolesPermissions = rolePermissionRepository.findByRoleIdAndPermissionId(roleId, permissionId);
        Role role = roleService.getRoleById(roleId);
        if(role == null){
            throw new NoRoleFoundException("no role found in database during delete role permissions", HttpStatus.BAD_REQUEST);
        }
        Permission permission = permissionService.getPermissionById(permissionId);
        if(permission == null){
            throw new NoPermissionFoundException("no permission found in database during delete permission", HttpStatus.BAD_REQUEST);
        }
        role.getRolePermissions().remove(deletedRolesPermissions);
        permission.getRolesPermissions().remove(deletedRolesPermissions);
        rolePermissionRepository.delete(deletedRolesPermissions);
        return toRolePermissionResponse(deletedRolesPermissions);
    }

}
