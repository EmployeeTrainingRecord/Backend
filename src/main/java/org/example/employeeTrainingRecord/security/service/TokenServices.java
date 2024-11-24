package org.example.employeeTrainingRecord.security.service;

import org.example.employeeTrainingRecord.database2.repositories.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenServices implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    public String getName(String token){
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String name = (String) jwtTokenUtil.getAllClaimsFromToken(token).get("name");
        return name;
    }
    public String getRole(String token){
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String name = (String) jwtTokenUtil.getAllClaimsFromToken(token).get("role");
        return name;
    }

    public String getOid(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return (String) jwtTokenUtil.getAllClaimsFromToken(token).get("oid");
    }
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return (UserDetails) userRepo.findByUsername(userName);
    }

}
