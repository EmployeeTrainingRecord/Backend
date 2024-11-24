package org.example.employeeTrainingRecord.database1.DTO;

import jakarta.annotation.Nullable;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class AddCourseDTO {
    private String course;
    private int trainingCost;
    private int totalHours;
}
