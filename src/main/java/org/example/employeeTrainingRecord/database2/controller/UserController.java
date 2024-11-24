package org.example.employeeTrainingRecord.database2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.employeeTrainingRecord.database2.DTO.AddNewUserDTO;
import org.example.employeeTrainingRecord.database2.entities.Users;
import org.example.employeeTrainingRecord.database2.services.UserService;
import org.example.employeeTrainingRecord.exception.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://ip23kp3.sit.kmutt.ac.th","http://intproj23.sit.kmutt.ac.th","http://ip23kp3.sit.kmutt.ac.th:3000","http://localhost:5173"})
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUser(@RequestHeader(value = "Authorization", required = false) String token){
        Object users = userService.getAllUsers(token);
        if (users instanceof List<?>){
            return ResponseEntity.status(HttpStatus.OK).body(users);
        }else{
            return errorResponse(users);
        }
    }
    @PostMapping
    public ResponseEntity<?> addUser(@RequestHeader(value = "Authorization", required = false) String token
            ,@RequestBody AddNewUserDTO addNewUserDTO) {
        Object addUser = userService.addUser(token,addNewUserDTO);
        if (addUser instanceof Users) {
            return ResponseEntity.status(HttpStatus.CREATED).body(addUser);
        }else {
            return errorResponse(addUser);
        }
    }
    @PutMapping("{userId}")
    public ResponseEntity<?> editUser(@RequestHeader(value = "Authorization", required = false) String token
            ,@PathVariable String userId , AddNewUserDTO addNewUserDTO){
        Object editedUser = userService.editUser(token,userId,addNewUserDTO);
        if (editedUser instanceof String){
            return errorResponse(editedUser);
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(editedUser);
        }
    }
    @DeleteMapping("{userId}")
    public ResponseEntity<?> deleteUser(@RequestHeader(value = "Authorization", required = false) String token,
                                        @PathVariable String userId){
        Object deletedUser = userService.deleteUser(token,userId);
        return ResponseEntity.status(HttpStatus.OK).body(deletedUser);
    }

    public ResponseEntity<?> errorResponse (Object body){
        return switch (body.toString()) {
            case "400" -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("BAD_REQUEST");
            case "401" -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
            case "403" -> ResponseEntity.status(HttpStatus.FORBIDDEN).body("FORBIDDEN");
            case "404" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT_FOUND");
            case "409" -> ResponseEntity.status(HttpStatus.CONFLICT).body("CONFLICT");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL_SERVER_ERROR");
        };
    }

}
