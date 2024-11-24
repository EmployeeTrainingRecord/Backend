package org.example.employeeTrainingRecord.database1.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "course")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    @Column(name = "courseId",length = 10)
    private String id;
    @Column(nullable = false, name = "course")
    private String course;
    @Column(name = "cost")
    private int cost;
    @Column(name = "totalHours")
    private int totalHours;
}