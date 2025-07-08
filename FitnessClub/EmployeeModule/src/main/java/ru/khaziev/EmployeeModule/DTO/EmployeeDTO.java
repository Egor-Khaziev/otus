package ru.khaziev.EmployeeModule.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class EmployeeDTO {
    private Long id;
    private String gender;
    private LocalDate birthday;
    private String firstName;
    private String patronymic;
    private String lastName;
    private BigDecimal salary;

    public String NamePlusPatronymic(){
        return firstName + " " + patronymic;
    }
}