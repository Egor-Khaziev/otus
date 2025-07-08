package ru.khaziev.EmployeeModule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import ru.khaziev.EmployeeModule.enums.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employees_seq_generator")
    @SequenceGenerator(name = "employees_seq_generator", sequenceName = "employees_seq", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender;

    private LocalDate birthday;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(length = 100)
    private String patronymic;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salary;
}