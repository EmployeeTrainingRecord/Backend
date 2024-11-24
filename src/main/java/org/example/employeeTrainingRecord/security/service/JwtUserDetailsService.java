package org.example.employeeTrainingRecord.security.service;

import org.example.employeeTrainingRecord.database2.entities.Users;
import org.example.employeeTrainingRecord.database2.repositories.UserRepo;
import org.example.employeeTrainingRecord.security.controller.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(userName);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, " username and password is incorrect !!");
        }

        UserDetails userDetails = new AuthUser(userName, user.getPassword());
        return userDetails;
    }
}

