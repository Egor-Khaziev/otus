package ru.khaziev.ScheduleModule.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ConcurrentSkipListMap;
import org.springframework.stereotype.Service;
import ru.khaziev.ScheduleModule.DTO.ScheduleDTO;
import ru.khaziev.ScheduleModule.entity.ClList;
import ru.khaziev.ScheduleModule.entity.Room;
import ru.khaziev.ScheduleModule.entity.Schedule;
import ru.khaziev.ScheduleModule.entity.TrainingType;
import ru.khaziev.ScheduleModule.repository.ClListRepository;
import ru.khaziev.ScheduleModule.repository.RoomRepository;
import ru.khaziev.ScheduleModule.repository.ScheduleRepository;
import ru.khaziev.ScheduleModule.repository.TrainingTypeRepository;

import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final RoomRepository roomRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final ClListRepository clListRepository;
    private final ModelMapper modelMapper;

//    private final ConcurrentSkipListMap<LocalDateTime, ScheduleDTO> scheduleCache = new ConcurrentSkipListMap<>();
//    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
//
//    @PostConstruct
//    public void initCache() {
//        readWriteLock.writeLock().lock();
//        try {
//            List<Schedule> schedules = scheduleRepository.findAll();
//            schedules.forEach(schedule -> {
//                ScheduleDTO scheduleDTO = modelMapper(schedule, ScheduleDTO.class); schedule -> modelMapper.map(schedule, ScheduleDTO.class)
//                scheduleCache.put(scheduleDTO.getDateTimeStart(), scheduleDTO);
//            });
//        } finally {
//            readWriteLock.writeLock().unlock();
//        }
//    }

    @Override
    @Transactional
    public ScheduleDTO createSchedule(Long employeeId, List<Long> clientIds, ScheduleDTO scheduleDTO) {
        Schedule schedule = modelMapper.map(scheduleDTO, Schedule.class);

        Room room = roomRepository.findById(scheduleDTO.getRoomId())
                .orElseThrow(() -> new NoSuchElementException("Room not found with id: " + scheduleDTO.getRoomId()));
        TrainingType trainingType = trainingTypeRepository.findById(scheduleDTO.getTrainingTypeId())
                .orElseThrow(() -> new NoSuchElementException("TrainingType not found with id: " + scheduleDTO.getTrainingTypeId()));

        schedule.setRoom(room);
        schedule.setTrainingType(trainingType);

        Schedule savedSchedule = scheduleRepository.save(schedule);

        List<ClList> clLists = clientIds.stream()
                .map(clientId -> {
                    ClList clList = new ClList();
                    clList.setClValue(clientId);
                    clList.setSchedule(savedSchedule);
                    return clList;
                })
                .collect(Collectors.toList());
        clListRepository.saveAll(clLists);

        savedSchedule.setClList(clLists);
        return modelMapper.map(savedSchedule, ScheduleDTO.class);
    }

    @Override
    public ScheduleDTO getScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Schedule not found with id: " + id));
        return modelMapper.map(schedule, ScheduleDTO.class);
    }

    @Override
    public List<ScheduleDTO> getScheduleByUserId(Long id) {
        List<Schedule> scheduleList = clListRepository.findClListsByClValue(id).stream().map(ClList::getSchedule).toList();
        return scheduleList.stream()
                .map(schedule -> modelMapper.map(schedule, ScheduleDTO.class))
                .toList();
    }

    @Override
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> scheduleList = scheduleRepository.findAll();
        return scheduleList.stream()
                .map(schedule -> modelMapper.map(schedule, ScheduleDTO.class))
                .toList();
    }

    @Override
    public List<ScheduleDTO> getTomorrowSchedules() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        LocalDateTime startOfDay = tomorrow.atStartOfDay();
        LocalDateTime endOfDay = tomorrow.atTime(LocalTime.MAX);

        List<Schedule> scheduleList = scheduleRepository.findByDateTimeStartBetween(startOfDay , endOfDay);
        return scheduleList.stream()
                .map(schedule -> modelMapper.map(schedule, ScheduleDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public ScheduleDTO updateSchedule(Long id, Long employeeId, List<Long> clientIds, ScheduleDTO scheduleDTO) {

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Schedule not found with id: " + id));

        schedule.setDateTimeStart(scheduleDTO.getDateTimeStart());
        schedule.setDateTimeEnd(scheduleDTO.getDateTimeEnd());
        schedule.setComment(scheduleDTO.getComment());
        schedule.setNumberTr(scheduleDTO.getNumberTr());

        Room room = roomRepository.findById(scheduleDTO.getRoomId())
                .orElseThrow(() -> new NoSuchElementException("Room not found with id: " + scheduleDTO.getRoomId()));
        TrainingType trainingType = trainingTypeRepository.findById(scheduleDTO.getTrainingTypeId())
                .orElseThrow(() -> new NoSuchElementException("TrainingType not found with id: " + scheduleDTO.getTrainingTypeId()));

        schedule.setRoom(room);
        schedule.setTrainingType(trainingType);

        // Update ClList
        // Remove old clList entries
        List<ClList> existingClLists = new ArrayList<>(schedule.getClList());

        for (ClList clList : existingClLists) {
            schedule.getClList().remove(clList);
        }
        // Create new clList entries
        List<ClList> newClLists = clientIds.stream()
                .map(clientId -> {
                    ClList clList = new ClList();
                    clList.setClValue(clientId);
                    clList.setSchedule(schedule);
                    return clList;
                })
                .collect(Collectors.toList());

        newClLists.forEach(schedule::addClList);

        Schedule updatedSchedule = scheduleRepository.save(schedule);

        ScheduleDTO updatedScheduleDTO = new ScheduleDTO();
        updatedScheduleDTO.setId(updatedSchedule.getId());
        updatedScheduleDTO.setDateTimeStart(updatedSchedule.getDateTimeStart());
        updatedScheduleDTO.setDateTimeEnd(updatedSchedule.getDateTimeEnd());
        updatedScheduleDTO.setRoomId(updatedSchedule.getRoom().getRoomId());
        updatedScheduleDTO.setTrainingTypeId(updatedSchedule.getTrainingType().getTrainingTypeId());
        updatedScheduleDTO.setComment(updatedSchedule.getComment());
        updatedScheduleDTO.setNumberTr(updatedSchedule.getNumberTr());

        return updatedScheduleDTO;
    }

    @Override
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }
}