package ru.khaziev.ScheduleModule.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleParticipantsDTO {
    private Long id;
    private Long employeeId;
    private List<Long> clientIds;
}