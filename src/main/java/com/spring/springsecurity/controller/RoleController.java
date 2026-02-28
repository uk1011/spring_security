package com.spring.springsecurity.controller;

import com.spring.springsecurity.entity.Role;
import com.spring.springsecurity.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("{user_id}")
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<List<Map<String,Object>>> getAllRoles(@PathVariable Integer user_id) {
        List<Map<String,Object>> allRolesOfUser = roleService.getAllRolesForUser(user_id);
        ResponseEntity.ok().body(allRolesOfUser);
        return new ResponseEntity<>(allRolesOfUser, HttpStatus.OK);
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String,Object>>> getAllRoles() {
        List<Map<String, Object>> allRoles = roleService.getAllRolesAdmin();
        return new ResponseEntity<>(allRoles, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String,Object>> createRole(@RequestBody Role role) {
        Map<String,Object> createdRole = roleService.createRole(role);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    @DeleteMapping("{role_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String,Object>> deleteRole(@PathVariable("role_id") int roleId) {
        Map<String,Object> deletedRole = roleService.deleteRoleById(roleId);
        return new ResponseEntity<>(deletedRole, HttpStatus.OK);
    }

}
