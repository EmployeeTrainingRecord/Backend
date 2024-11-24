package org.example.employeeTrainingRecord.security.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JwtRequestUser {
    @NotBlank
    @NotNull
    @Size(max = 50)
    private String userName;
    @Size(max = 14)
    @NotBlank
    @NotNull
    private String password;
}
