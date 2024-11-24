package org.example.employeeTrainingRecord.database1.repositories;

import org.example.employeeTrainingRecord.database1.entities.TrainingLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingLogRepo extends JpaRepository<TrainingLog, String> {
    List<TrainingLog> findAllByEmployeeId(String employeeId);
    TrainingLog findByEmployeeId(String employeeId);
}
