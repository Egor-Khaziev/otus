package ru.khaziev.ScheduleModule.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.khaziev.ScheduleModule.DTO.TrainingTypeDTO;
import ru.khaziev.ScheduleModule.entity.Schedule;
import ru.khaziev.ScheduleModule.entity.TrainingType;
import ru.khaziev.ScheduleModule.repository.ScheduleRepository;
import ru.khaziev.ScheduleModule.repository.TrainingTypeRepository;

import java.util.List;
import java.util.NoSuchElementException;
import org.modelmapper.ModelMapper;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;
    private final ModelMapper modelMapper;
    private final ScheduleRepository scheduleRepository;

    @Override
    public TrainingTypeDTO createTrainingType(TrainingTypeDTO trainingTypeDTO) {
        TrainingType trainingType = modelMapper.map(trainingTypeDTO, TrainingType.class);
        TrainingType savedTrainingType = trainingTypeRepository.save(trainingType);
        return modelMapper.map(savedTrainingType, TrainingTypeDTO.class);
    }

    @Override
    public TrainingTypeDTO getTrainingTypeById(Integer id) {
        TrainingType trainingType = trainingTypeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("TrainingType not found with id: " + id));
        return modelMapper.map(trainingType, TrainingTypeDTO.class);
    }

    @Override
    public List<TrainingTypeDTO> getAllTrainingTypes() {
        List<TrainingType> trainingTypes = trainingTypeRepository.findAll();
        return trainingTypes.stream()
                .map(trainingType -> modelMapper.map(trainingType, TrainingTypeDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public TrainingTypeDTO updateTrainingType(Integer id, TrainingTypeDTO trainingTypeDTO) {
        TrainingType trainingType = trainingTypeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("TrainingType not found with id: " + id));
        modelMapper.map(trainingTypeDTO, trainingType);
        trainingType.setTrainingTypeId(id);
        TrainingType updatedTrainingType = trainingTypeRepository.save(trainingType);
        return modelMapper.map(updatedTrainingType, TrainingTypeDTO.class);
    }

    @Override
    public void deleteTrainingType(Integer id) {
        TrainingType trainingType = trainingTypeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("TrainingType not found with id: " + id));

        // find schedules with training type
        List<Schedule> schedules = scheduleRepository.findScheduleByTrainingType(trainingType); // Assuming you have this query

        scheduleRepository.deleteAll(schedules);
        trainingTypeRepository.deleteById(id);
    }
}