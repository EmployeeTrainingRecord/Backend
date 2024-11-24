package org.example.employeeTrainingRecord.database1.services;

import org.example.employeeTrainingRecord.GlobalServices;
import org.example.employeeTrainingRecord.database1.DTO.AddTrainingLogDTO;
import org.example.employeeTrainingRecord.database1.DTO.TrainingLogDTO;
import org.example.employeeTrainingRecord.database1.entities.TrainingLog;
import org.example.employeeTrainingRecord.database1.repositories.TrainingLogRepo;
import org.example.employeeTrainingRecord.database2.entities.Users;
import org.example.employeeTrainingRecord.database2.repositories.UserRepo;
import org.example.employeeTrainingRecord.database2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service

public class TrainingLogService {
    @Autowired
    private TrainingLogRepo trainingLogRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private GlobalServices globalServices;

    public List<TrainingLogDTO> getTrainingLogs() {
        List<TrainingLog> trainingLogs = trainingLogRepo.findAll();
        return trainingLogs.stream()
                .map(trainingLog -> new TrainingLogDTO(trainingLog.getId(), trainingLog.getName(), trainingLog.getCourse(),trainingLog.getTotalHours(),trainingLog.getTrainingCost()))
                .collect(Collectors.toList());
    }
    public Object getMyLogs(String token){
        String oid = globalServices.getValueFromToken(token,"oid");
        if (oid.equals("401")){
            return "401";
        }
        List<TrainingLog> trainingLogs = trainingLogRepo.findAllByEmployeeId(oid);
        return trainingLogs.stream()
                .map(trainingLog -> new TrainingLogDTO(trainingLog.getId(), trainingLog.getName(), trainingLog.getCourse(),trainingLog.getTotalHours(),trainingLog.getTrainingCost()))
                .collect(Collectors.toList());
    }
    public  Object getLogDetail(String token,String logId){
        String role = globalServices.getValueFromToken(token,"role");
        if (role.equals("401")) {
            return "401";
        }
        TrainingLog log = trainingLogRepo.findById(logId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "logId does not exist !!!"));
        if (role.equals("admin") || role.equals("manager") || log.getEmployeeId().equals(globalServices.getValueFromToken(token,"oid"))){
            return log;
        }
            return "403";
    }
    public Object addLog(String token ,AddTrainingLogDTO addTrainingLogDTO) {
        String newId;
        Optional<TrainingLog> existingLog;
        do {
            newId = UUID.randomUUID().toString().substring(0, 11).replace("-", "");
            existingLog = trainingLogRepo.findById(newId);
        } while (existingLog.isPresent());
        String role = globalServices.getValueFromToken(token,"role");
        if (role.equals("401")){
            return "401";
        }else if (role.equals("manager") || role.equals("admin")) {
            if (addTrainingLogDTO.getEmployeeId() == null || addTrainingLogDTO.getEmployeeId().isEmpty()){
                return "400";
            }
            Users user = userRepo.findById(addTrainingLogDTO.getEmployeeId()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "employeeId does not exist !!!"));
            TrainingLog newLog = new TrainingLog(newId, user.getOid(), user.getName(), addTrainingLogDTO.getCourse(), addTrainingLogDTO.getTotalHours(),addTrainingLogDTO.getTrainingCost(), null, null, addTrainingLogDTO.getData());
            trainingLogRepo.save(newLog);
            return newLog;
        }
        TrainingLog newLog = new TrainingLog(newId, globalServices.getValueFromToken(token,"oid"), globalServices.getValueFromToken(token,"name"), addTrainingLogDTO.getCourse(), addTrainingLogDTO.getTotalHours(),addTrainingLogDTO.getTrainingCost(), null, null, addTrainingLogDTO.getData());
        trainingLogRepo.save(newLog);
        return newLog;
    }

    public Object editLog(String token,String logId, AddTrainingLogDTO addTrainingLogDTO) {
        String role = globalServices.getValueFromToken(token,"role");
        if (role.equals("401")){
            return "401";
        }
        TrainingLog log = trainingLogRepo.findById(logId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "logId does not exist !!!"));
        if (role.equals("admin") || role.equals("manager")){
            if (addTrainingLogDTO.getEmployeeId() == null || addTrainingLogDTO.getEmployeeId() .isEmpty()){
                return "400";
            }
            Users user = userRepo.findById(addTrainingLogDTO.getEmployeeId()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "employeeId does not exist !!!"));
            log.setEmployeeId(addTrainingLogDTO.getEmployeeId());
            log.setName(user.getName());
        }else if (log.getEmployeeId().equals(globalServices.getValueFromToken(token,"oid"))){
            log.setEmployeeId(globalServices.getValueFromToken(token , "oid"));
            log.setName(globalServices.getValueFromToken(token,"name"));
        }else {
            return "403";
        }
        log.setCourse(addTrainingLogDTO.getCourse());
        log.setTotalHours(addTrainingLogDTO.getTotalHours());
        log.setTrainingCost(addTrainingLogDTO.getTrainingCost());
        log.setData(addTrainingLogDTO.getData());
        trainingLogRepo.save(log);
        return log;
    }
    public Object deleteLog(String token,String logId){
        String role = globalServices.getValueFromToken(token,"role");
        if (role.equals("401")) {
            return "401";
        }
        TrainingLog log = trainingLogRepo.findById(logId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "logId does not exist !!!"));
        if (role.equals("admin") || role.equals("manager") || log.getEmployeeId().equals(globalServices.getValueFromToken(token,"oid"))){
            trainingLogRepo.deleteById(log.getId());
            return "200";
        }
        return "403";
    }
}
