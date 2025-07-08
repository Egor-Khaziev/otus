package ru.khaziev.TelegramBotModule.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.khaziev.TelegramBotModule.DTO.ScheduleDTO;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import ru.khaziev.TelegramBotModule.connector.ScheduleConnector;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleConnector scheduleConnector;

    @Override
    public List<ScheduleDTO> getScheduleByUserId(long id) throws IOException {
        return scheduleConnector.getScheduleByUserId(id);
    }

    public List<ScheduleDTO> getTomorrowSchedules() throws IOException {
        return scheduleConnector.getTomorrowSchedules();
    }
}
