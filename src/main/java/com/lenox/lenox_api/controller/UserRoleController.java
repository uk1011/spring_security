package com.lenox.lenox_api.controller;

import com.lenox.lenox_api.entity.UserRole;
import com.lenox.lenox_api.entity.compositekeys.UserRoleKey;
import com.lenox.lenox_api.service.UserRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin/user_role")
public class UserRoleController {

    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getAllUserRoles(){
        List<Map<String, Object>> allUsersRoles = userRoleService.getAll();
        return new ResponseEntity<>(allUsersRoles, HttpStatus.OK);
    }

    @GetMapping("{userId}")
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getAllRolesRelatedToUser(@PathVariable Integer userId){
        List<Map<String, Object>> rolesPermissions = userRoleService.getByUserId(userId);
        return new ResponseEntity<>(rolesPermissions, HttpStatus.OK);
    }

    @PostMapping("/{userId}/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> createUserRoles(@PathVariable Integer userId,@PathVariable Integer roleId){
        Map<String, Object> createdUserRoles = userRoleService.saveUserRoles(userId, roleId);
        return new ResponseEntity<>(createdUserRoles, HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteUserRoles(@PathVariable Integer userId, @PathVariable Integer roleId){
        Map<String, Object> deletedUserRoles = userRoleService.deleteUserRoles(userId, roleId);
        return new ResponseEntity<>(deletedUserRoles, HttpStatus.OK);
    }

    @PutMapping("/{userId}/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateUserRoles(@PathVariable Integer userId, @PathVariable Integer roleId){
        Map<String, Object> updatedUserRoles = userRoleService.updateUserRoles(userId,roleId);
        return new ResponseEntity<>(updatedUserRoles, HttpStatus.OK);
    }

}
