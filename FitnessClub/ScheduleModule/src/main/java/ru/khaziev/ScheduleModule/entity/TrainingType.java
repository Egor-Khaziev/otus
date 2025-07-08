package ru.khaziev.ScheduleModule.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "training_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Assuming auto-increment
    @Column(name = "training_type_id")
    private Integer trainingTypeId;

    @Column(name = "training_type_name", nullable = false, unique = true)
    private String trainingTypeName;
}