package ru.khaziev.TelegramBotModule.service;

import ru.khaziev.TelegramBotModule.DTO.ClientDTO;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ClientService {

    void updateClient(Long id, ClientDTO client) throws IOException;

    ClientDTO getClientByPhone(String phoneNumber) throws IOException;

    ClientDTO getClient(long clid) throws IOException;

    ClientDTO getClientByTelegramUser(String userName) throws IOException;
    ClientDTO getClientByTelegramChat(String chat)  throws IOException;

    ClientDTO[] getAllClients() throws IOException;
}
