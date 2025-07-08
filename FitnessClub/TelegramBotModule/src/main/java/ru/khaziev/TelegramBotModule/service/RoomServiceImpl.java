package ru.khaziev.TelegramBotModule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khaziev.TelegramBotModule.DTO.RoomDTO;
import ru.khaziev.TelegramBotModule.connector.RoomConnector;

import java.io.IOException;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomConnector roomConnector;

    @Override
    public RoomDTO getRoomById(long id) throws IOException {
        return roomConnector.getRoomById(id);
    }
}
