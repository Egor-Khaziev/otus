package ru.khaziev.EmployeeModule.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDTO {
    private Long operationId;
    private String operationPublicId;
    private String operationName;
    private BigDecimal operationAmount;
    private String operationType;
    private Long employeeId;
}