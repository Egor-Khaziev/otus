package ru.khaziev.ScheduleModule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khaziev.ScheduleModule.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
}