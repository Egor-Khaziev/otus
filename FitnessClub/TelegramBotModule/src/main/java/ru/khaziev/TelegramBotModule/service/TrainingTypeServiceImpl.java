package ru.khaziev.TelegramBotModule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khaziev.TelegramBotModule.DTO.TrainingTypeDTO;
import ru.khaziev.TelegramBotModule.connector.TrainingTypeConnector;

import java.io.IOException;

@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {
    @Autowired
    private TrainingTypeConnector trainingTypeConnector;

    @Override
    public TrainingTypeDTO getTrainingTypeById(long id) throws IOException {
        return trainingTypeConnector.getTrainingTypeById(id);
    }
}
