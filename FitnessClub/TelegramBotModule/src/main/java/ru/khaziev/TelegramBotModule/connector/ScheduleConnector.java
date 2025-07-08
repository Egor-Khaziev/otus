package ru.khaziev.TelegramBotModule.connector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import ru.khaziev.TelegramBotModule.DTO.ScheduleDTO;

import java.io.IOException;
import java.util.List;

@Component
public class ScheduleConnector {

//    private final String baseUrl = "http://gateway:8080";
    private final String baseUrl = "http://localhost:8080";
    @Autowired
    private HttpRequest connector;
    @Autowired
    private ObjectMapper objectMapper;
/*
    public ScheduleConnector(String baseUrl) {
        this.baseUrl = baseUrl;
        this.connector = new HttpRequest();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        // Add any specific date/time format configurations here if needed
    }
*/
    public ScheduleConnector() {
    }

    public ScheduleDTO createSchedule(Long employeeId, List<Long> clientIds, ScheduleDTO scheduleDTO) throws IOException {
        String apiUrl = UriComponentsBuilder.fromHttpUrl(baseUrl + "/api/schedules")
                .queryParam("employeeId", employeeId)
                .queryParam("clientIds", clientIds)
                .toUriString();

        String response = connector.sendPostRequest(apiUrl, scheduleDTO, null);

        if (response != null) {
            return objectMapper.readValue(response, ScheduleDTO.class);
        } else {
            return null;
        }
    }

    public ScheduleDTO getScheduleById(Long id) throws IOException {
        String apiUrl = baseUrl + "/api/schedules/" + id;
        String response = connector.sendGetRequest(apiUrl, null);

        if (response != null) {
            return objectMapper.readValue(response, ScheduleDTO.class);
        } else {
            return null;
        }
    }

    public List<ScheduleDTO> getScheduleByUserId(Long id) throws IOException {
        String apiUrl = baseUrl + "/api/schedules/user/" + id;
        String response = connector.sendGetRequest(apiUrl, null);

        if (response != null) {
            return objectMapper.readValue(response, new TypeReference<List<ScheduleDTO>>() {});
        } else {
            return null;
        }
    }

    public ScheduleDTO[] getAllSchedules() throws IOException {
        String apiUrl = baseUrl + "/api/schedules";
        String response = connector.sendGetRequest(apiUrl, null);

        if (response != null) {
            return objectMapper.readValue(response, ScheduleDTO[].class);
        } else {
            return null;
        }
    }

    public List<ScheduleDTO> getTomorrowSchedules() throws IOException {
        String apiUrl = baseUrl + "/api/schedules/tomorrow";
        String response = connector.sendGetRequest(apiUrl, null);

        if (response != null) {
            return objectMapper.readValue(response, new TypeReference<List<ScheduleDTO>>() {});
        } else {
            return null;
        }
    }

    public ScheduleDTO updateSchedule(Long id, Long employeeId, List<Long> clientIds, ScheduleDTO scheduleDTO) throws IOException {
        String apiUrl = UriComponentsBuilder.fromHttpUrl(baseUrl + "/api/schedules/" + id)
                .queryParam("employeeId", employeeId)
                .queryParam("clientIds", clientIds)
                .toUriString();

        String response = connector.sendPutRequest(apiUrl, scheduleDTO, null);

        if (response != null) {
            return objectMapper.readValue(response, ScheduleDTO.class);
        } else {
            return null;
        }
    }

    public boolean deleteSchedule(Long id) throws IOException {
        String apiUrl = baseUrl + "/api/schedules/" + id;
        String response = connector.sendDeleteRequest(apiUrl, null);
        return response != null;
    }
/*
    public static void main(String[] args) {
        String baseUrl = "http://localhost:8083"; // Replace with your Schedule API's base URL
        ScheduleConnector scheduleConnector = new ScheduleConnector(baseUrl);

        try {
            // 1. Create
            Long employeeId = 123L;  // Example employee ID

            ScheduleDTO newScheduleDTO = new ScheduleDTO();
            newScheduleDTO.setDateTimeStart(LocalDateTime.now());
            newScheduleDTO.setDateTimeEnd(LocalDateTime.now().plusHours(2));
            newScheduleDTO.setRoomId(1); // Example room ID
            newScheduleDTO.setTrainingTypeId(1); // Example training type ID
            newScheduleDTO.setComment("Initial schedule");
            newScheduleDTO.setNumberTr(12345L); // Example training number
            newScheduleDTO.setClListIds(Arrays.asList(456L, 789L)); // Example client list IDs

            System.out.println("Before creating schedule: " + newScheduleDTO);

            ScheduleDTO createdScheduleDTO = scheduleConnector.createSchedule(employeeId, newScheduleDTO.getClListIds(), newScheduleDTO);
            System.out.println("After creating schedule: " + createdScheduleDTO);

            // 2. Get by ID
            if (createdScheduleDTO != null) {
                Long scheduleId = createdScheduleDTO.getId();
                ScheduleDTO retrievedScheduleDTO = scheduleConnector.getScheduleById(scheduleId);
                System.out.println("Retrieved schedule: " + retrievedScheduleDTO);

                // 3. Update
                ScheduleDTO updatedScheduleDTO = new ScheduleDTO();
                updatedScheduleDTO.setId(scheduleId);  // MUST set the ID for updates
                updatedScheduleDTO.setDateTimeStart(LocalDateTime.now().plusDays(1)); // Change start time
                updatedScheduleDTO.setDateTimeEnd(LocalDateTime.now().plusDays(1).plusHours(3));
                updatedScheduleDTO.setRoomId(2); // Change room ID
                updatedScheduleDTO.setTrainingTypeId(2); // Change training type ID
                updatedScheduleDTO.setComment("Updated schedule");
                updatedScheduleDTO.setNumberTr(54321L); // Updated training number
                updatedScheduleDTO.setClListIds(Arrays.asList(111L, 222L)); // Updated client list IDs

                ScheduleDTO reallyUpdatedScheduleDTO = scheduleConnector.updateSchedule(scheduleId, employeeId, updatedScheduleDTO.getClListIds(), updatedScheduleDTO);
                System.out.println("Updated schedule: " + reallyUpdatedScheduleDTO);

                // 4. Get All
                ScheduleDTO[] allSchedules = scheduleConnector.getAllSchedules();
                System.out.println("All schedules:");
                if (allSchedules != null) {
                    for (ScheduleDTO schedule : allSchedules) {
                        System.out.println(schedule);
                    }
                } else {
                    System.out.println("No schedules found.");
                }

                // 5. Delete
                boolean deleted = scheduleConnector.deleteSchedule(scheduleId);
                System.out.println("Schedule deleted: " + deleted);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
}