package ru.khaziev.TelegramBotModule.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.khaziev.TelegramBotModule.DTO.RoomDTO;

import java.io.IOException;

@Component
public class RoomConnector {

//    private final String baseUrl = "http://gateway:8080";
    private final String baseUrl = "http://localhost:8080";
    @Autowired
    private HttpRequest connector;
    @Autowired
    private ObjectMapper objectMapper;

    public RoomConnector() {
    }

    public RoomDTO getRoomById(Long id) throws IOException {
        String apiUrl = baseUrl + "/api/rooms/" + id;
        String response = connector.sendGetRequest(apiUrl, null);

        if (response != null) {
            return objectMapper.readValue(response, RoomDTO.class);
        } else {
            return null;
        }
    }
}