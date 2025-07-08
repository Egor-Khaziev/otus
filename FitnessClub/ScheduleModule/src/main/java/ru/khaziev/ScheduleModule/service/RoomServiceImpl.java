package ru.khaziev.ScheduleModule.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.khaziev.ScheduleModule.DTO.RoomDTO;
import ru.khaziev.ScheduleModule.entity.Room;
import ru.khaziev.ScheduleModule.entity.Schedule;
import ru.khaziev.ScheduleModule.repository.RoomRepository;

import org.modelmapper.ModelMapper;
import ru.khaziev.ScheduleModule.repository.ScheduleRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final ScheduleRepository scheduleRepository;

    @Override
    public RoomDTO createRoom(RoomDTO roomDTO) {
        Room room = modelMapper.map(roomDTO, Room.class);
        Room savedRoom = roomRepository.save(room);
        return modelMapper.map(savedRoom, RoomDTO.class);
    }

    @Override
    public RoomDTO getRoomById(Integer id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Room not found with id: " + id));
        return modelMapper.map(room, RoomDTO.class);
    }

    @Override
    public List<RoomDTO> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream()
                .map(room -> modelMapper.map(room, RoomDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDTO updateRoom(Integer id, RoomDTO roomDTO) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Room not found with id: " + id));
        modelMapper.map(roomDTO, room);
        room.setRoomId(id);
        Room updatedRoom = roomRepository.save(room);
        return modelMapper.map(updatedRoom, RoomDTO.class);
    }

    @Override
    @Transactional
    public void deleteRoom(Integer id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Room not found with id: " + id));

        // find schedules with room
        List<Schedule> schedules = scheduleRepository.findScheduleByRoom(room);

        // Set room_id = null
        schedules.forEach(schedule -> schedule.setRoom(null));
        scheduleRepository.saveAll(schedules);

        // delete room
        roomRepository.deleteById(id);
    }
}