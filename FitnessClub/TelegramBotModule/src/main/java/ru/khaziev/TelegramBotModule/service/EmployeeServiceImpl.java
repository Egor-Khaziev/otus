package ru.khaziev.TelegramBotModule.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.khaziev.TelegramBotModule.DTO.EmployeeDTO;

import org.springframework.stereotype.Service;
import ru.khaziev.TelegramBotModule.connector.EmployeeConnector;

import java.io.IOException;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeConnector employeeConnector;

    @Override
    public EmployeeDTO getEmployee(long trID) throws IOException {
        return employeeConnector.getEmployee(trID);
    }
}
