package com.spring.springsecurity.service;

import com.spring.springsecurity.entity.Role;
import com.spring.springsecurity.entity.User;
import com.spring.springsecurity.entity.UserRole;
import com.spring.springsecurity.globalexception.exceptions.NoRoleFoundException;
import com.spring.springsecurity.globalexception.exceptions.NoUserRoleMappingFoundException;
import com.spring.springsecurity.globalexception.exceptions.UsersNotFoundException;
import com.spring.springsecurity.repository.RoleRepository;
import com.spring.springsecurity.repository.UserRepository;
import com.spring.springsecurity.repository.UserRoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserRoleService {


    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public UserRoleService(UserRoleRepository userRoleRepository, RoleRepository roleRepository, UserRepository userRepository) {
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public Map<String, Object> toUserRoleResponse(UserRole userRole) {
        LinkedHashMap<String, Object> userRoleResponse = new LinkedHashMap<>();
        userRoleResponse.put("user_id", userRole.getUser().getUserId());
        userRoleResponse.put("user_name", userRole.getUser().getFirstName());
        Set<String> roles = new LinkedHashSet<>();
        List<Role> roleList = userRoleRepository.findAllRolesByUserId(userRole.getUser().getUserId());
        if(!roleList.isEmpty()){
            for(Role role : roleList){
                roles.add(role.getRoleName());
            }
        }
        userRoleResponse.put("roles_of_user", roles);
        userRoleResponse.put("created_at", userRole.getCreatedAt());
        userRoleResponse.put("created_by", userRole.getCreatedBy());
        userRoleResponse.put("updated_at", userRole.getUpdatedAt());
        userRoleResponse.put("updated_by", userRole.getUpdatedBy());
        return userRoleResponse;
    }

    public UserRole toUserRole(User user, Role role) {
        return UserRole.builder()
                .user(user)
                .role(role)
                .build();
    }

    public List<Map<String, Object>> getByUserId(Integer userId){
        List<UserRole> userRole = userRoleRepository.findByUserId(userId);
        if(userRole!=null){
            return userRole.stream()
                    .map(this::toUserRoleResponse)
                    .collect(Collectors.toList());
        }
        throw new NoUserRoleMappingFoundException("no mapping with role found for user during fetch roles related to user", HttpStatus.OK);
    }

    public List<Map<String, Object>> getAll(){
        List<UserRole> allUserRoles = userRoleRepository.findAll();
        if(allUserRoles.isEmpty()){
            throw new  NoUserRoleMappingFoundException("no mappings of any user with any role found during fetch all user roles", HttpStatus.OK);
        }
        return allUserRoles.stream()
                .map(this::toUserRoleResponse)
                .collect(Collectors.toList());
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Map<String, Object> saveUserRoles(Integer userId, Integer roleId){
        Optional<User> user =  userRepository.findById(userId);
        if(user.isEmpty()){
            throw new UsersNotFoundException("user not found in database during user role save", HttpStatus.NOT_FOUND);
        }
        User dbUser = user.get();
        Optional<Role> role = roleRepository.findById(roleId);
        if(role.isEmpty()){
            throw new NoRoleFoundException("role not found in database during user role save", HttpStatus.NOT_FOUND);
        }
        Role dbRole = role.get();
        List<Role> userRoles = userRoleRepository.findAllRolesByUserId(userId);
        if(userRoles.isEmpty()){
            throw new NoUserRoleMappingFoundException("no mapping with role found for user during user role save", HttpStatus.OK);
        }
        for(Role role1 : userRoles){
            if(role1.getRoleName().equals(role.get().getRoleName())){
                return Map.of(
                        "message", "Role already exists",
                        "user", user.get().getFirstName(),
                        "role_name", role.get().getRoleName()
                );
            }
        }
        UserRole userRole = toUserRole(dbUser, dbRole);
        dbUser.getUserRoleList().add(userRole);
        userRepository.save(dbUser);
        dbRole.getUserRoles().add(userRole);
        roleRepository.save(dbRole);
        UserRole userRole1= userRoleRepository.save(userRole);
        return toUserRoleResponse(userRole1);
    }

    @Transactional()
    public Map<String, Object> deleteUserRoles(Integer userId, Integer roleId){
        UserRole deletedUserRole = userRoleRepository.findByUserIdAndRoleId(userId, roleId);
        if(deletedUserRole!=null){
            User user = userRepository.findById(userId).get();
            Role role = roleRepository.findById(roleId).get();
            user.getUserRoleList().remove(deletedUserRole);
            userRepository.save(user);
            role.getUserRoles().remove(deletedUserRole);
            roleRepository.save(role);
            userRoleRepository.delete(deletedUserRole);
            return toUserRoleResponse(deletedUserRole);
        }
        throw new  NoUserRoleMappingFoundException("no mapping found for this user and role id during user role delete", HttpStatus.OK);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Map<String, Object> updateUserRoles(Integer userId,Integer roleId){
        List<Role> oldUserRoles = userRoleRepository.findAllRolesByUserId(userId);
        Optional<User> dbUser = userRepository.findById(userId);
        if(dbUser.isEmpty()){
            throw new UsersNotFoundException("user not found in database during user role update", HttpStatus.NOT_FOUND);
        }
        Optional<Role> newRole = roleRepository.findById(roleId);
        if(newRole.isEmpty()){
            throw new NoRoleFoundException("role not found in database during user role update", HttpStatus.NOT_FOUND);
        }
        if(oldUserRoles!=null){
            for(Role oldRole : oldUserRoles){
                if(oldRole.getRoleName().equals(newRole.get().getRoleName())){
                    return Map.of(
                            "message" , "user role mapping already exists",
                            "user", dbUser.get().getFirstName(),
                            "role_name", newRole.get().getRoleName()
                    );
                }
            }
            saveUserRoles(userId, roleId);
        }
        throw new NoUserRoleMappingFoundException("no mapping with role found for user during user role update", HttpStatus.OK);
    }

}
