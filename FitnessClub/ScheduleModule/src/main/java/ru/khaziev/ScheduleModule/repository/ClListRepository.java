package ru.khaziev.ScheduleModule.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.khaziev.ScheduleModule.entity.ClList;
import ru.khaziev.ScheduleModule.entity.Schedule;

import java.util.List;

@Repository
public interface ClListRepository extends CrudRepository<ClList, Integer> {
    List<ClList> findClListsByClValue(Long clValue);
}
