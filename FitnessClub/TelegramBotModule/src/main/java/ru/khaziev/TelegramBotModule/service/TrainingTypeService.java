package ru.khaziev.TelegramBotModule.service;

import org.springframework.stereotype.Service;
import ru.khaziev.TelegramBotModule.DTO.TrainingTypeDTO;

import java.io.IOException;

@Service
public interface TrainingTypeService {
    TrainingTypeDTO getTrainingTypeById(long id) throws IOException;
}
