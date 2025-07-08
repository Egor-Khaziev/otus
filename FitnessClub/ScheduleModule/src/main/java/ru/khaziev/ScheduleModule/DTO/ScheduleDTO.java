package ru.khaziev.ScheduleModule.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ScheduleDTO {

    private Long id;
    private LocalDateTime dateTimeStart;
    private LocalDateTime dateTimeEnd;
    private Integer roomId;
    private Integer trainingTypeId;
    private String comment;
    private long numberTr;
    private List<Long> clListIds;

    public void setClListIds(List<Long> clListIds) {
        this.clListIds = clListIds;
    }

}