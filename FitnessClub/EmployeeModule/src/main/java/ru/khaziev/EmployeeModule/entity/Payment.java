package ru.khaziev.EmployeeModule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import ru.khaziev.EmployeeModule.enums.OperationType;

import java.math.BigDecimal;

@Entity
@Table(name = "payments_emp")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payments_seq_generator")
    @SequenceGenerator(name = "payments_seq_generator", sequenceName = "payments_seq", allocationSize = 1)
    @Column(name = "operation_id")
    private Long operationId;

    @Column(name = "operation_public_id", length = 200)
    private String operationPublicId;

    @Column(name = "operation_name", length = 200)
    private String operationName;

    @Column(name = "operation_amount", precision = 10, scale = 2)
    private BigDecimal operationAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false, length = 20)
    private OperationType operationType;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

}