package ru.khaziev.TelegramBotModule.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

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