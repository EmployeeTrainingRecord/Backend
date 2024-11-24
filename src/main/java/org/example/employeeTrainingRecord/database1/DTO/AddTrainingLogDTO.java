package org.example.employeeTrainingRecord.database1.DTO;

import jakarta.annotation.Nullable;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class AddTrainingLogDTO {
    @Nullable
    private String employeeId;
    private String course;
    private String totalHours;
    private int TrainingCost;
    @Nullable
    @Lob
    private byte[] data;
}
