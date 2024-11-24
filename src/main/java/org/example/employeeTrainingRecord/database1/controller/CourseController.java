package org.example.employeeTrainingRecord.database1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.employeeTrainingRecord.database1.DTO.AddCourseDTO;
import org.example.employeeTrainingRecord.database1.DTO.AddTrainingLogDTO;
import org.example.employeeTrainingRecord.database1.DTO.TrainingLogDTO;
import org.example.employeeTrainingRecord.database1.entities.Course;
import org.example.employeeTrainingRecord.database1.services.CourseService;
import org.example.employeeTrainingRecord.database1.services.TrainingLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"})
@RequestMapping("/course")
public class CourseController {
@Autowired
private CourseService courseService;
    @GetMapping()
    public ResponseEntity<?> getCourse() {
        List<Course> trainingLogs = courseService.getCourse();
        if (trainingLogs != null){
            return ResponseEntity.status(HttpStatus.OK).body(trainingLogs);
        }else{
            return errorResponse("404");
        }
    }
    @PostMapping("")
    public ResponseEntity<?> addNewLog(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody AddCourseDTO addCourseDTO){
        try {
            Object newCourse = courseService.addCourse(token,addCourseDTO);
            if(newCourse instanceof String){
                return errorResponse(newCourse);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(newCourse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    @PutMapping("{courseId}")
    public ResponseEntity<?> editLog(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody AddCourseDTO addCourseDTO,@PathVariable String courseId){
        try {
            Object newLog = courseService.editCourse(token,courseId,addCourseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(newLog);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    @DeleteMapping("{courseId}")
    public ResponseEntity<?> deleteLog(@RequestHeader(value = "Authorization", required = false) String token,
                                       @PathVariable String courseId){
        try {
            Object deleteLog = courseService.deleteCourse(token,courseId);
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
