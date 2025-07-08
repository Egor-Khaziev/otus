package ru.khaziev.TelegramBotModule.service;

import ru.khaziev.TelegramBotModule.DTO.ScheduleDTO;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface ScheduleService {

    List<ScheduleDTO> getScheduleByUserId(long id) throws IOException;
    List<ScheduleDTO> getTomorrowSchedules() throws IOException;

}
