package ru.khaziev.ScheduleModule.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ClientDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private LocalDate birthday;
    private String gender;
    private BigDecimal wallet;
    private String phoneNumber;
    private String telegramUser;
    private String telegramChat;

}