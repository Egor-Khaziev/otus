package ru.khaziev.ScheduleModule.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.khaziev.ScheduleModule.DTO.TrainingTypeDTO;
import ru.khaziev.ScheduleModule.service.TrainingTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/training-types")
@RequiredArgsConstructor
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;

    @PostMapping
    public ResponseEntity<TrainingTypeDTO> createTrainingType(@RequestBody TrainingTypeDTO trainingTypeDTO) {
        TrainingTypeDTO createdTrainingType = trainingTypeService.createTrainingType(trainingTypeDTO);
        return new ResponseEntity<>(createdTrainingType, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingTypeDTO> getTrainingTypeById(@PathVariable Integer id) {
        TrainingTypeDTO trainingType = trainingTypeService.getTrainingTypeById(id);
        return new ResponseEntity<>(trainingType, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TrainingTypeDTO>> getAllTrainingTypes() {
        List<TrainingTypeDTO> trainingTypes = trainingTypeService.getAllTrainingTypes();
        return new ResponseEntity<>(trainingTypes, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainingTypeDTO> updateTrainingType(@PathVariable Integer id, @RequestBody TrainingTypeDTO trainingTypeDTO) {
        TrainingTypeDTO updatedTrainingType = trainingTypeService.updateTrainingType(id, trainingTypeDTO);
        return new ResponseEntity<>(updatedTrainingType, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainingType(@PathVariable Integer id) {
        trainingTypeService.deleteTrainingType(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
