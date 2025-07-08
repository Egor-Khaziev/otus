package ru.khaziev.ScheduleModule.service;

import ru.khaziev.ScheduleModule.DTO.RoomDTO;

import java.util.List;

public interface RoomService {
    RoomDTO createRoom(RoomDTO roomDTO);
    RoomDTO getRoomById(Integer id);
    List<RoomDTO> getAllRooms();
    RoomDTO updateRoom(Integer id, RoomDTO roomDTO);
    void deleteRoom(Integer id);
}
