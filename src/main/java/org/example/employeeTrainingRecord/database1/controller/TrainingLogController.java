package org.example.employeeTrainingRecord.database1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.employeeTrainingRecord.database1.DTO.AddTrainingLogDTO;
import org.example.employeeTrainingRecord.database1.DTO.TrainingLogDTO;
import org.example.employeeTrainingRecord.database1.entities.TrainingLog;
import org.example.employeeTrainingRecord.database1.services.TrainingLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://ip23kp3.sit.kmutt.ac.th","http://intproj23.sit.kmutt.ac.th","http://ip23kp3.sit.kmutt.ac.th:3000","http://localhost:5173"})
@RequestMapping("/log")
public class TrainingLogController {
@Autowired
private TrainingLogService trainingLogService;
    @GetMapping()
    public ResponseEntity<?> getListInvite() {
        List<TrainingLogDTO> trainingLogs = trainingLogService.getTrainingLogs();
        if (trainingLogs != null){
            return ResponseEntity.status(HttpStatus.OK).body(trainingLogs);
        }else{
            return errorResponse("404");
        }
    }
    @GetMapping("{logId}")
    public ResponseEntity<?> getLogDetail(@RequestHeader(value = "Authorization", required = false) String token,@PathVariable String logId){
        Object trainingLogs = trainingLogService.getLogDetail(token,logId);
        if (trainingLogs instanceof String){
            return errorResponse(trainingLogs);
        }
            return ResponseEntity.status(HttpStatus.OK).body(trainingLogs);
    }
    @GetMapping("myLog")
    public ResponseEntity<?> getMyLog(@RequestHeader(value = "Authorization", required = false) String token){
        Object trainingLogs = trainingLogService.getMyLogs(token);
        if (trainingLogs instanceof String){
            return errorResponse(trainingLogs);
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(trainingLogs);
        }
    }
    @PostMapping("")
    public ResponseEntity<?> addNewLog(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestPart("trainingLogDTO") String trainingLogDTOString,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            AddTrainingLogDTO trainingLogDTO = objectMapper.readValue(trainingLogDTOString, AddTrainingLogDTO.class);
            if (file != null) {
                trainingLogDTO.setData(file.getBytes());
            }
            Object newLog = trainingLogService.addLog(token,trainingLogDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newLog);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    @PutMapping("{logId}")
    public ResponseEntity<?> editLog(
            @RequestHeader(value = "Authorization") String token,
            @RequestPart("trainingLogDTO") String trainingLogDTOString,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @PathVariable String logId) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            AddTrainingLogDTO trainingLogDTO = objectMapper.readValue(trainingLogDTOString, AddTrainingLogDTO.class);
            if (file != null) {
                trainingLogDTO.setData(file.getBytes());
            }
            Object newLog = trainingLogService.editLog(token,logId,trainingLogDTO);
            return ResponseEntity.status(HttpStatus.OK).body(newLog);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    @DeleteMapping("{logId}")
    public ResponseEntity<?> deleteLog(@RequestHeader(value = "Authorization", required = false) String token,
                                       @PathVariable String logId){
        try {
            Object deleteLog = trainingLogService.deleteLog(token,logId);
            if (deleteLog.equals("200")){
                return ResponseEntity.status(HttpStatus.OK).body("200");
            }
            return errorResponse(deleteLog);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
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
