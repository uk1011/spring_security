package com.spring.springsecurity.config;

import com.spring.springsecurity.entity.User;
import com.spring.springsecurity.entity.UserPrincipal;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if(authentication == null || targetDomainObject == null || permission == null) {
            return false;
        }
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String permissionName = (String) permission;
        if(targetDomainObject instanceof User) {
            User user = (User) targetDomainObject;
            Integer userId = user.getUserId();
            if(!userPrincipal.getId().equals(userId)) {
                return false;
            }
            for(GrantedAuthority authorities : userPrincipal.getAuthorities()) {
                if(authorities.getAuthority().equals(permissionName)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if(authentication == null || targetId == null || permission == null || targetType == null) {
            return false;
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String permissionName = (String) permission;
        if(targetType.equals("User")) {
            if(!userPrincipal.getId().equals(targetId)) {
                return false;
            }
            for(GrantedAuthority authorities : userPrincipal.getAuthorities()) {
                if(authorities.getAuthority().equals(permissionName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
