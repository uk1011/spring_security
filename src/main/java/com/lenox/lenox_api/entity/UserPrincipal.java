package com.lenox.lenox_api.entity;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private final Integer id;
    private final String username;
    private final String password;
    private final String email;
    private final Boolean isPasswordChangeRequired;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(User user) {
        this.id = user.getUserId();
        this.username = user.getFirstName();
        this.password = user.getHashedPassword();
        this.email = user.getEmail();
        this.isPasswordChangeRequired = user.getIsPasswordChangeRequired();
        Set<String> authority = new HashSet<String>();
        for(UserRole userRole : user.getUserRoleList()){
            Role role = userRole.getRole();
            authority.add("ROLE_" + role.getRoleName());
            for(RolePermission rolePermission : role.getRolePermissions()) {
                Permission permission = rolePermission.getPermission();
                authority.add(permission.getPermissionName());
            }
        }
        this.authorities = authority.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    public Boolean getPasswordChangeRequired() {
        return isPasswordChangeRequired;
    }

    public Integer getId() {
        return id;
    }
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
