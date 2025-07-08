package ru.khaziev.TelegramBotModule.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.khaziev.TelegramBotModule.DTO.TrainingTypeDTO;

import java.io.IOException;

@Component
public class TrainingTypeConnector {

//    private final String baseUrl = "http://gateway:8080";
    private final String baseUrl = "http://localhost:8080";
    @Autowired
    private HttpRequest connector;
    @Autowired
    private ObjectMapper objectMapper;

    public TrainingTypeConnector() {
    }

    public TrainingTypeDTO getTrainingTypeById(Long id) throws IOException {
        String apiUrl = baseUrl + "/api/training-types/" + id;
        String response = connector.sendGetRequest(apiUrl, null);

        if (response != null) {
            return objectMapper.readValue(response, TrainingTypeDTO.class);
        } else {
            return null;
        }
    }
}
