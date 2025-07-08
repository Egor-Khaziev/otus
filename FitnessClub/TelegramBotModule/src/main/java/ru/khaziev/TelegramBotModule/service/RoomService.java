package ru.khaziev.TelegramBotModule.service;

import org.springframework.stereotype.Service;
import ru.khaziev.TelegramBotModule.DTO.RoomDTO;

import java.io.IOException;

@Service
public interface RoomService {

    RoomDTO getRoomById(long id) throws IOException;
}
