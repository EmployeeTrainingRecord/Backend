package org.example.employeeTrainingRecord.database2.repositories;

import org.example.employeeTrainingRecord.database2.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Users, String> {
    Users findByUsername(String username);
}
