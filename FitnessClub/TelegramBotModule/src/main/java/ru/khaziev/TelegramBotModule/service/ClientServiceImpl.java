package ru.khaziev.TelegramBotModule.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.khaziev.TelegramBotModule.DTO.ClientDTO;

import org.springframework.stereotype.Service;
import ru.khaziev.TelegramBotModule.connector.ClientConnector;

import java.io.IOException;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientConnector clientConnector;

    @Override
    public void updateClient(Long id, ClientDTO client) throws IOException {
        clientConnector.updateClient(id,client);
    }

    @Override
    public ClientDTO getClientByPhone(String phoneNumber) throws IOException {
        return clientConnector.getClientByPhone(phoneNumber);
    }

    @Override
    public ClientDTO getClient(long clid) throws IOException {
        return clientConnector.getClient(clid);
    }

    @Override
    public ClientDTO getClientByTelegramUser(String userName)  throws IOException {
        return clientConnector.getClientByTelegramUser(userName);
    }

    @Override
    public ClientDTO getClientByTelegramChat(String chat)  throws IOException {
        return clientConnector.getClientByTelegramChat(chat);
    }

    @Override
    public ClientDTO[] getAllClients()  throws IOException {
        return clientConnector.getAllClients();
    }

}
