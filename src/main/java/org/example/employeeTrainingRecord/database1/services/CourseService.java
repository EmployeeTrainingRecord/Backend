package org.example.employeeTrainingRecord.database1.services;

import org.example.employeeTrainingRecord.GlobalServices;
import org.example.employeeTrainingRecord.database1.DTO.AddCourseDTO;
import org.example.employeeTrainingRecord.database1.DTO.AddTrainingLogDTO;
import org.example.employeeTrainingRecord.database1.DTO.TrainingLogDTO;
import org.example.employeeTrainingRecord.database1.entities.Course;
import org.example.employeeTrainingRecord.database1.entities.TrainingLog;
import org.example.employeeTrainingRecord.database1.repositories.CourseRepo;
import org.example.employeeTrainingRecord.database1.repositories.TrainingLogRepo;
import org.example.employeeTrainingRecord.database2.entities.Users;
import org.example.employeeTrainingRecord.database2.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service

public class CourseService {
    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private GlobalServices globalServices;

    public List<Course> getCourse() {
        return courseRepo.findAll();
    }
    public Object addCourse(String token , AddCourseDTO addCourseDTO) {
        String newId;
        Optional<Course> existingCourse;
        do {
            newId = UUID.randomUUID().toString().substring(0, 11).replace("-", "");
            existingCourse = courseRepo.findById(newId);
        } while (existingCourse.isPresent());
        String role = globalServices.getValueFromToken(token,"role");
        if (role.equals("401")) {
            return "401";
        }else {
            Course course = new Course(newId,addCourseDTO.getCourse(),addCourseDTO.getTrainingCost(),addCourseDTO.getTotalHours());
            courseRepo.save(course);
            return course;
        }
    }

    public Object editCourse(String token,String courseId, AddCourseDTO addCourseDTO) {
        String role = globalServices.getValueFromToken(token,"role");
        if (role.equals("401")){
            return "401";
        }
        Course course = courseRepo.findById(courseId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "logId does not exist !!!"));
        course.setCourse(addCourseDTO.getCourse());
        course.setCost(addCourseDTO.getTrainingCost());
        course.setTotalHours(addCourseDTO.getTotalHours());
        courseRepo.save(course);
        return course;
    }
    public Object deleteCourse(String token,String courseId){
        String role = globalServices.getValueFromToken(token,"role");
        if (role.equals("401")) {
            return "401";
        }
        Course course = courseRepo.findById(courseId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "logId does not exist !!!"));
            courseRepo.deleteById(course.getId());
            return "200";
    }
}
