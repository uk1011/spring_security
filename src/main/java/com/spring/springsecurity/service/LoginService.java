package com.spring.springsecurity.service;

import com.spring.springsecurity.entity.User;
import com.spring.springsecurity.entity.UserPrincipal;
import com.spring.springsecurity.globalexception.exceptions.PasswordResetRequiredException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class LoginService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRoleService userRoleService;
    private final UserService userService;

    public LoginService(JwtService jwtService, AuthenticationManager authenticationManager, UserRoleService userRoleService, UserService userService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userRoleService = userRoleService;
        this.userService = userService;
    }

    public String verifyUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        if(principal.getPasswordChangeRequired() == true) {
            throw new PasswordResetRequiredException("reset your password first then login with new credentials", HttpStatus.BAD_REQUEST);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(username);
        }
        throw new RuntimeException("Authentication Failed - bad credentials");
    }

    @Transactional
    public Map<String,Object> createAdmin(User user){
        Map<String, Object> userDetails = userService.createAdmin(user);
        userRoleService.saveUserRoles((Integer) userDetails.get("id"), 1);
        return userService.findById((Integer) userDetails.get("id"));
    }

    public Map<String, Object> changePassword(String username, String oldPassword, String newPassword){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        if(authentication.isAuthenticated()){
            return userService.changePassword(username, newPassword);
        }
        throw new RuntimeException("Authentication Failed - bad credentials");
    }

}
