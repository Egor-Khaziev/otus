package ru.khaziev.ScheduleModule.service;

import ru.khaziev.ScheduleModule.DTO.TrainingTypeDTO;

import java.util.List;

public interface TrainingTypeService {
    TrainingTypeDTO createTrainingType(TrainingTypeDTO trainingTypeDTO);
    TrainingTypeDTO getTrainingTypeById(Integer id);
    List<TrainingTypeDTO> getAllTrainingTypes();
    TrainingTypeDTO updateTrainingType(Integer id, TrainingTypeDTO trainingTypeDTO);
    void deleteTrainingType(Integer id);
}
