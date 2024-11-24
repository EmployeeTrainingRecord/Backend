package org.example.employeeTrainingRecord.database1.repositories;

import org.example.employeeTrainingRecord.database1.entities.Course;
import org.example.employeeTrainingRecord.database1.entities.TrainingLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepo extends JpaRepository<Course, String> {
}
