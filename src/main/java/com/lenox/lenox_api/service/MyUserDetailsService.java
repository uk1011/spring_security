package com.lenox.lenox_api.service;

import com.lenox.lenox_api.entity.User;
import com.lenox.lenox_api.entity.UserPrincipal;
import com.lenox.lenox_api.globalexception.exceptions.UsersNotFoundException;
import com.lenox.lenox_api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public  MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByfirstName(username);
        if(user == null){
            throw new UsersNotFoundException("user not found during userdetails for spring security creation", HttpStatus.BAD_REQUEST);
        }
        return new UserPrincipal(user);
    }
}
