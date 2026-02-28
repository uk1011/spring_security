package com.spring.springsecurity.service;


import com.spring.springsecurity.entity.Permission;
import com.spring.springsecurity.globalexception.exceptions.NoPermissionFoundException;
import com.spring.springsecurity.repository.PermissionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    public Map<String, Object> toPermissionResponse(Permission permission) {
        LinkedHashMap<String, Object> permissionResponse = new LinkedHashMap<>();
        permissionResponse.put("permission_id", permission.getPermissionId());
        permissionResponse.put("name", permission.getPermissionName());
        permissionResponse.put("scope", permission.getScope());
        permissionResponse.put("created_at", permission.getCreatedAt());
        permissionResponse.put("created_by", permission.getCreatedBy());
        permissionResponse.put("updated_at", permission.getUpdatedAt());
        permissionResponse.put("updated_by", permission.getUpdatedBy());
        return permissionResponse;
    }

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

//    @PreAuthorize("hasRole('ADMIN')")
    public List<Map<String, Object>> getAllPermissions() {

        List<Permission> allPermission = permissionRepository.findAll();

        if(allPermission.isEmpty()) {
            throw new NoPermissionFoundException("no permission found in database during fetch all permissions operation", HttpStatus.NO_CONTENT);
        }
        return allPermission.stream()
                .map(this::toPermissionResponse)
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getPermissionForRole(Integer roleId) {
        List<Permission> permissions = permissionRepository.getPermissionsByRole(roleId);
        if(permissions.isEmpty()) {
            throw new NoPermissionFoundException("no permission associated with the role", HttpStatus.NO_CONTENT);
        }
        return permissions.stream()
                .map(this::toPermissionResponse)
                .collect(Collectors.toList());
    }

    public Permission getPermissionById(Integer permissionId) {
        Optional<Permission> permission = permissionRepository.findById(permissionId);
        if(permission.isEmpty()) {
            throw new NoPermissionFoundException("no permission found with this permission id", HttpStatus.NO_CONTENT);
        }
        return permission.get();
    }

}
