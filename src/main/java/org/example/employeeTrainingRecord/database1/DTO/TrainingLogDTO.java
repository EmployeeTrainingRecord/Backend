package org.example.employeeTrainingRecord.database1.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class TrainingLogDTO {
    private String logId;
    private String name;
    private String course;
    private String totalHours;
    private int TrainingCost;
}
