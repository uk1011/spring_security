package com.spring.springsecurity.controller;

import com.spring.springsecurity.entity.User;
import com.spring.springsecurity.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class LoginController {

    private LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login/{username}/{password}")
    public String login(@PathVariable String username, @PathVariable String password) {
        return loginService.verifyUser(username, password);
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String,Object>> signup(@RequestBody User user){
        Map<String,Object> user1 = loginService.createAdmin(user);
        return new ResponseEntity<>(user1, HttpStatus.OK);
    }

    @PostMapping("/changepassword/{username}/{password}/{newPassword}")
    @PreAuthorize("principal.getPasswordChangeRequired")
    public ResponseEntity<Map<String,Object>> changePassword(@PathVariable String username, @PathVariable String password, @PathVariable String newPassword){
        Map<String, Object> user = loginService.changePassword(username, password, newPassword);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


}
