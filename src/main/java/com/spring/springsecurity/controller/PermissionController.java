package com.spring.springsecurity.controller;

import com.spring.springsecurity.service.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/permission")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getPermissions() {
        List<Map<String, Object>> all_permissions = permissionService.getAllPermissions();
        return new ResponseEntity<>(all_permissions, HttpStatus.OK);
    }

    @GetMapping("{role_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getPermissionForRole(@PathVariable (name = "role_id") Integer roleId){
        List<Map<String, Object>> permissions = permissionService.getPermissionForRole(roleId);
        return new ResponseEntity<>(permissions, HttpStatus.OK);
    }

}
