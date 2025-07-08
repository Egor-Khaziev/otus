package ru.khaziev.ScheduleModule.service;

import ru.khaziev.ScheduleModule.DTO.ClListDTO;
import ru.khaziev.ScheduleModule.DTO.ScheduleDTO;

import java.util.List;

public interface ClListService {
    ClListDTO createClList(ClListDTO clListDTO);
    ClListDTO getClListById(Long id);
    List<ClListDTO> getAllClLists();
    ClListDTO updateClList(Long id, ClListDTO clListDTO);
    void deleteClList(Long id);
}