package org.example.employeeTrainingRecord.database2.services;

import org.example.employeeTrainingRecord.GlobalServices;
import org.example.employeeTrainingRecord.database1.entities.TrainingLog;
import org.example.employeeTrainingRecord.database2.DTO.AddNewUserDTO;
import org.example.employeeTrainingRecord.database2.entities.Users;
import org.example.employeeTrainingRecord.database2.repositories.UserRepo;
import org.example.employeeTrainingRecord.security.service.TokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service

public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private GlobalServices globalServices;


    public Object getAllUsers(String token) {
        String role = globalServices.getValueFromToken(token,"role");
        if (role.equals("401")){
            return "401";
        }else if (!(role.equals("admin") || (role.equals("manager")))){
            return "403";
        }
        return userRepo.findAll();
    }
    public Object addUser(String token,AddNewUserDTO addNewUserDTO){
        if (isRoleAdmin(token) != null){
            return isRoleAdmin(token);
        }
        String newId;
        Optional<Users> existingLog;
        do {
            newId = UUID.randomUUID().toString().substring(0, 14).replace("-", "");
            existingLog = userRepo.findById(newId);
        } while (existingLog.isPresent());
        Argon2 argon2 = Argon2Factory.create();
        String hash = argon2.hash(2, 65536, 1, addNewUserDTO.getPassword());
        if (userRepo.findByUsername(addNewUserDTO.getUsername()) != null){
            return "409";
        }
        if(!(addNewUserDTO.getRole().equals("admin") || addNewUserDTO.getRole().equals("manager") || addNewUserDTO.getRole().equals("employee"))){
            return "400";
        }
        Users newUser = new Users(newId, addNewUserDTO.getUsername(), hash, addNewUserDTO.getName(), addNewUserDTO.getRole());
        userRepo.save(newUser);
        userRepo.flush();
        return newUser;
    }
    public Object editUser(String token ,String userId ,AddNewUserDTO addNewUserDTO){
        if (isRoleAdmin(token) != null){
            return isRoleAdmin(token);
        }
        Users  user = userRepo.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "logId does not exist !!!"));
        user.setName(addNewUserDTO.getName());
        user.setUsername(addNewUserDTO.getUsername());
        user.setPassword(addNewUserDTO.getPassword());
        user.setRole(addNewUserDTO.getRole());
        userRepo.save(user);
        return user;
    }
    public Object deleteUser(String token, String userId){
        if (isRoleAdmin(token) != null){
            return isRoleAdmin(token);
        }
        Users user = userRepo.findById(userId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "logId does not exist !!!"));
        userRepo.deleteById(user.getOid());
        return "200";
    }
    public String isRoleAdmin (String token){
        String role = globalServices.getValueFromToken(token,"role");
        if (role.equals("401")){
            return "401";
        }else if (!role.equals("admin")){
            return "403";
        }
        return null;
    }
}
