package ru.khaziev.ScheduleModule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.khaziev.ScheduleModule.entity.ClList;
import ru.khaziev.ScheduleModule.entity.Room;
import ru.khaziev.ScheduleModule.entity.Schedule;
import ru.khaziev.ScheduleModule.entity.TrainingType;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findScheduleByRoom(Room room);
    List<Schedule> findScheduleByTrainingType(TrainingType trainingType);
    List<Schedule> findByDateTimeStartBetween(java.time.LocalDateTime start, java.time.LocalDateTime end);

//    @Query("SELECT s FROM Schedule s LEFT JOIN s.clList cl WHERE cl.id IN :clientListIds")
    @Query(value = "SELECT s.* FROM Schedule s LEFT JOIN cl_List cl on s.id = cl.schedule_id WHERE cl.cl_value = :clientListIds",
            nativeQuery = true)
    List<Schedule> findScheduleByClList(@Param("clientListIds") List<Long> clientListIds);

}