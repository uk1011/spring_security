package com.spring.springsecurity.service;

import com.spring.springsecurity.entity.*;
import com.spring.springsecurity.entity.Role;
import com.spring.springsecurity.entity.RolePermission;
import com.spring.springsecurity.entity.User;
import com.spring.springsecurity.entity.UserRole;
import com.spring.springsecurity.globalexception.exceptions.UsersNotFoundException;
import com.spring.springsecurity.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public static Map<String, Object> toUserResponse(User user) {
        LinkedHashMap<String, Object> userResponse = new LinkedHashMap<>();
        userResponse.put("id",user.getUserId());
        userResponse.put("username",user.getFirstName());
        userResponse.put("fullname",user.getFirstName()+" "+user.getLastName());
        userResponse.put("email",user.getEmail());
        userResponse.put("phoneNumber",user.getPhoneNumber());
        if(user.getUserRoleList() != null && !user.getUserRoleList().isEmpty()) {
            Set<String> roles = new HashSet<>();
            Set<String> permissions = new HashSet<>();
            for(UserRole userRole : user.getUserRoleList()){
                Role role = userRole.getRole();
                roles.add(role.getRoleName());
                Set<RolePermission> rolePermissions = role.getRolePermissions();
                if(rolePermissions != null && !rolePermissions.isEmpty()) {
                    for(RolePermission rolePermission : rolePermissions) {
                        permissions.add(rolePermission.getPermission().getPermissionName());
                    }
                }
            }
            userResponse.put("roles",roles);
            userResponse.put("permissions",permissions);
        }
        userResponse.put("created_at",user.getCreatedAt());
        userResponse.put("created_by",user.getCreatedBy());
        userResponse.put("updated_at",user.getUpdatedAt());
        userResponse.put("updated_by",user.getUpdatedBy());
        return userResponse;
    }

    public Map<String, Object> findById(Integer id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            return  toUserResponse(user.get());
        }
        throw new UsersNotFoundException("user not found during user fetch by id", HttpStatus.NOT_FOUND);
    }

    @Transactional(propagation =  Propagation.REQUIRED)
    public Map<String, Object> createAdmin(User user) {
        Role role = roleService.getRoleById(1);
        user.setIsPasswordChangeRequired(false);
        UserRole userRole = new UserRole();
        userRole.setRole(role);
        userRole.setUser(user);
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setUserRoleList(userRoles);
        User user1 = userRepository.save(user);
        return toUserResponse(user1);
    }

    @Transactional
    public Map<String, Object> createUser(User user) {
        user.setHashedPassword(passwordEncoder.encode(user.getHashedPassword()));
        Role role = roleService.getRoleById(2);
        user.setIsPasswordChangeRequired(true);
        UserRole userRole = new UserRole();
        userRole.setRole(role);
        userRole.setUser(user);
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setUserRoleList(userRoles);
        User user1 = userRepository.save(user);
        return toUserResponse(user1);
    }

    @Transactional
    public Map<String, Object> changePassword(String username, String newPassword) {
        Optional<User> user = Optional.ofNullable(userRepository.findByfirstName(username));
        if(user.isEmpty()){
            throw new UsersNotFoundException("User not found for password reset during reset password", HttpStatus.NOT_FOUND);
        }
        User user1 = user.get();
        user1.setHashedPassword(passwordEncoder.encode(newPassword));
        user1.setIsPasswordChangeRequired(false);
        User userSaved = userRepository.save(user1);
        return toUserResponse(userSaved);
    }

    public List<Map<String, Object>> findAllUsers() {
        List<User> users = userRepository.findAll();
        if(users.isEmpty()) {
            throw new UsersNotFoundException("user not found during all users fetch", HttpStatus.NO_CONTENT);
        }
        return users.stream()
                .map(UserService::toUserResponse)
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public Map<String, Object> updateUserDetails(Integer id, User updatedUser) {
        Optional<User> userInDb = userRepository.findById(id);
        if (userInDb.isPresent()) {
            User user = userInDb.get();
            if(updatedUser.getFirstName() != null) {
                user.setFirstName(updatedUser.getFirstName());
            }
            if(updatedUser.getLastName() != null) {
                user.setLastName(updatedUser.getLastName());
            }
            if(updatedUser.getEmail() != null) {
                user.setEmail(updatedUser.getEmail());
            }
            if(updatedUser.getPhoneNumber() != null) {
                user.setPhoneNumber(updatedUser.getPhoneNumber());
            }
            return toUserResponse(userRepository.save(user));
        }
        throw new UsersNotFoundException("user not found during user details update", HttpStatus.NO_CONTENT);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Map deleteUser(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return toUserResponse(user.get());
        }
        throw new UsersNotFoundException("user not found during user delete operation", HttpStatus.NO_CONTENT);
    }

}
