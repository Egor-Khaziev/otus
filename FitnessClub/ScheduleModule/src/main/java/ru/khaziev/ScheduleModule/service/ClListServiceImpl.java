package ru.khaziev.ScheduleModule.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.khaziev.ScheduleModule.DTO.ClListDTO;
import ru.khaziev.ScheduleModule.DTO.ScheduleDTO;
import ru.khaziev.ScheduleModule.entity.ClList;
import ru.khaziev.ScheduleModule.entity.Schedule;
import ru.khaziev.ScheduleModule.repository.ClListRepository;
import ru.khaziev.ScheduleModule.repository.ScheduleRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ClListServiceImpl implements ClListService {

    private final ClListRepository clListRepository;
    private final ScheduleRepository scheduleRepository;  // Need this to set the Schedule relationship
    private final ModelMapper modelMapper;

    @Override
    public ClListDTO createClList(ClListDTO clListDTO) {
        ClList clList = modelMapper.map(clListDTO, ClList.class);

        Schedule schedule = scheduleRepository.findById(clListDTO.getScheduleId())
                .orElseThrow(() -> new NoSuchElementException("Schedule not found with id: " + clListDTO.getScheduleId()));
        clList.setSchedule(schedule); // Set the relationship

        ClList savedClList = clListRepository.save(clList);
        return modelMapper.map(savedClList, ClListDTO.class);
    }

    @Override
    public ClListDTO getClListById(Long id) {
        ClList clList = clListRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new NoSuchElementException("ClList not found with id: " + id));
        return modelMapper.map(clList, ClListDTO.class);
    }

    @Override
    public List<ClListDTO> getAllClLists() {
        List<ClList> clLists = StreamSupport.stream(clListRepository.findAll().spliterator(), false)
                .toList();
        return clLists.stream()
                .map(clList -> modelMapper.map(clList, ClListDTO.class))
                .collect(Collectors.toList());
    }


    @Override
    public ClListDTO updateClList(Long id, ClListDTO clListDTO) {
        ClList clList = clListRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new NoSuchElementException("ClList not found with id: " + id));

        clList.setClValue(clListDTO.getClValue());

        Schedule schedule = scheduleRepository.findById(clListDTO.getScheduleId())
                .orElseThrow(() -> new NoSuchElementException("Schedule not found with id: " + clListDTO.getScheduleId()));
        clList.setSchedule(schedule);

        ClList updatedClList = clListRepository.save(clList);
        return modelMapper.map(updatedClList, ClListDTO.class);
    }

    @Override
    public void deleteClList(Long id) {
        clListRepository.deleteById(Math.toIntExact(id));
    }
}