package com.spring.springsecurity.service;

import com.spring.springsecurity.entity.Role;
import com.spring.springsecurity.entity.RolePermission;
import com.spring.springsecurity.globalexception.exceptions.NoRoleFoundException;
import com.spring.springsecurity.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleService {

    public static Map<String, Object> toRoleResponse(Role role) {
        LinkedHashMap<String, Object> roleResponse = new LinkedHashMap<>();
        roleResponse.put("roleId", role.getRoleId());
        roleResponse.put("roleName", role.getRoleName());
        if(role.getRolePermissions() != null) {
            Set<String> permissions = new HashSet<>();
            for(RolePermission rolePermission : role.getRolePermissions()) {
                permissions.add(rolePermission.getPermission().getPermissionName());
            }
            roleResponse.put("permissions", permissions);
        }
        roleResponse.put("createdAt", role.getCreatedAt());
        roleResponse.put("createdBy", role.getCreatedBy());
        roleResponse.put("updatedAt", role.getUpdatedAt());
        roleResponse.put("updatedBy", role.getUpdatedBy());
        return roleResponse;
    }

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional()
    public List<Map<String, Object>> getAllRolesAdmin() {
        List<Role> roles = roleRepository.findAll();
        if(roles.isEmpty()) {
            throw new NoRoleFoundException("no roles found in database during fetching of all roles", HttpStatus.NO_CONTENT);
        }
        return roles.stream()
                .map(RoleService::toRoleResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAllRolesForUser(Integer userId) {
        List<Role> roles = roleRepository.findAllRolesForUser(userId);
        if(roles.isEmpty()) {
            throw new NoRoleFoundException("no roles found related to user in database during fetching all roles related to user", HttpStatus.NO_CONTENT);
        }
        return roles.stream()
                .map(RoleService::toRoleResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> createRole(Role role) {
        Role roleCreated = roleRepository.save(role);
        if(roleCreated == null) {
            throw new RuntimeException("Role could not be created");
        }
        return toRoleResponse(roleCreated);
    }

    public Role getRoleById(Integer id) {
        Optional<Role> role = roleRepository.findById(id);
        if(role.isPresent()) {
            return role.get();
        }
        throw new NoRoleFoundException("no role found in database during fetch role by id", HttpStatus.NO_CONTENT);
    }

    @Transactional
    public Map<String, Object> deleteRoleById(int id) {

        Optional<Role> roleInDb = roleRepository.findById(id);
        if (roleInDb.isPresent()) {
            roleRepository.delete(roleInDb.get());
            return toRoleResponse(roleInDb.get());
        }
        throw new NoRoleFoundException("no role found in database during delete role", HttpStatus.NO_CONTENT);
    }

}
