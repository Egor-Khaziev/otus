package ru.khaziev.TelegramBotModule.service;

import ru.khaziev.TelegramBotModule.DTO.EmployeeDTO;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface EmployeeService {

    EmployeeDTO getEmployee(long trID) throws IOException;
}
