package ru.khaziev.ScheduleModule.service;

import ru.khaziev.ScheduleModule.DTO.ScheduleDTO;

import java.util.List;

public interface ScheduleService {
    ScheduleDTO createSchedule(Long employeeId, List<Long> clientIds, ScheduleDTO scheduleDTO);
    ScheduleDTO getScheduleById(Long id);
    List<ScheduleDTO> getScheduleByUserId(Long id);
    List<ScheduleDTO> getAllSchedules();
    List<ScheduleDTO> getTomorrowSchedules();
    ScheduleDTO updateSchedule(Long id, Long employeeId, List<Long> clientIds, ScheduleDTO scheduleDTO);
    void deleteSchedule(Long id);
}