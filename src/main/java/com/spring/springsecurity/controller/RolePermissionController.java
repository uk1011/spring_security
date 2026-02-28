package com.spring.springsecurity.controller;

import com.spring.springsecurity.service.RolePermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/admin/role_permission")
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    public RolePermissionController(RolePermissionService rolePermissionService){
        this.rolePermissionService = rolePermissionService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> findAllRolesPermissions(){
        List<Map<String, Object>> allRolePermissions = rolePermissionService.getAllRolePermissions();
        return new ResponseEntity<>(allRolePermissions, HttpStatus.OK);
    }

    @GetMapping("/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getRolePermissions(@PathVariable Integer roleId){
        List<Map<String, Object>> rolesPermissions = rolePermissionService.getRolePermissionsById(roleId);
        return new ResponseEntity<>(rolesPermissions, HttpStatus.OK);
    }

    @PostMapping("/{roleId}/{permissionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> createRolePermissions(@PathVariable Integer roleId, @PathVariable Integer permissionId){
        Map<String, Object> createdRolesPermissions = rolePermissionService.createRolePermissions(roleId, permissionId);
        return new ResponseEntity<>(createdRolesPermissions, HttpStatus.CREATED);
    }

    @PutMapping("/{roleId}/{permissionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateRolePermissions(@PathVariable Integer roleId,@PathVariable Integer permissionId){
        Map<String, Object> updatedRolePermission = rolePermissionService.updateRolesPermissions(roleId, permissionId);
        return new ResponseEntity<>(updatedRolePermission, HttpStatus.OK);
    }

    @DeleteMapping("/{roleId}/{permissionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteRolePermissions(@PathVariable Integer roleId, @PathVariable Integer permissionId){
        Map<String, Object> deletedRolePermission = rolePermissionService.deleteRolePermissions(roleId, permissionId);
        return new ResponseEntity<>(deletedRolePermission, HttpStatus.OK);
    }


}
