package org.example.employeeTrainingRecord.database2.DTO;

import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class AddNewUserDTO {
    private String username;
    private String password;
    private String name;
    private String role;
}
