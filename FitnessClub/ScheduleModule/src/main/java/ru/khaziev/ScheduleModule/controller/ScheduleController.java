package ru.khaziev.ScheduleModule.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.khaziev.ScheduleModule.DTO.ScheduleDTO;
import ru.khaziev.ScheduleModule.service.ScheduleService;


import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleDTO> createSchedule(
            @RequestParam Long employeeId,
            @RequestParam List<Long> clientIds,
            @RequestBody ScheduleDTO scheduleDTO) {
        ScheduleDTO createdSchedule = scheduleService.createSchedule(employeeId, clientIds, scheduleDTO);
        return new ResponseEntity<>(createdSchedule, HttpStatus.CREATED);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ScheduleDTO>> getScheduleByUserId(@PathVariable Long id) {
        List<ScheduleDTO> scheduleList = scheduleService.getScheduleByUserId(id);
        return new ResponseEntity<>(scheduleList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO> getScheduleById(@PathVariable Long id) {
        ScheduleDTO schedule = scheduleService.getScheduleById(id);
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleDTO>> getAllSchedules() {
        List<ScheduleDTO> schedules = scheduleService.getAllSchedules();
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDTO> updateSchedule(
            @PathVariable Long id,
            @RequestParam Long employeeId,
            @RequestParam List<Long> clientIds,
            @RequestBody ScheduleDTO scheduleDTO) {
        ScheduleDTO updatedSchedule = scheduleService.updateSchedule(id, employeeId, clientIds, scheduleDTO);
        return new ResponseEntity<>(updatedSchedule, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/tomorrow")
    public ResponseEntity<List<ScheduleDTO>> getTomorrowSchedules() {
        List<ScheduleDTO> schedules = scheduleService.getTomorrowSchedules();
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }
}