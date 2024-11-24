package org.example.employeeTrainingRecord.database1.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "training_log")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TrainingLog {
    @Id
    @Column(name = "recordId",length = 10)
    private String id;
    @Column(nullable = false, name = "employeeId",length = 13)
    private String employeeId;
    @Column(name = "name")
    private String name;
    @Column(name = "course")
    private String course;
    @Column(name = "TotalHours")
    private String totalHours;
    @Column(name = "TrainingCost")
    private int TrainingCost;
    @Column(name = "recorded_on" , insertable = false , updatable = false)
    private Date recordedOn ;
    @Column(name = "updated_on", insertable = false , updatable = false)
    private Date updatedOn;
    @Lob
    @Column(name = "file_data", columnDefinition = "LONGBLOB")
    private byte[] data;
}