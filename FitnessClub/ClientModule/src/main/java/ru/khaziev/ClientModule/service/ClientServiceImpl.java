package ru.khaziev.ClientModule.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.khaziev.ClientModule.DTO.ClientDTO;
import ru.khaziev.ClientModule.entity.Client;
import ru.khaziev.ClientModule.enums.Gender;
import ru.khaziev.ClientModule.repository.ClientRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Класс Сервис для работы с клиентом
 *
 * @author Khegori
 * @version 1.0
 */
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @PostConstruct
    public void init()     {
    }


    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Метод для получения полного списка клиентов
     *
     * @return возвращает Лист всех клиентов
     */
    @Override
    public Optional<List<ClientDTO>> findAllClients() {
        List<Client> clients = clientRepository.findAll();
        if (clients.isEmpty()) {
            return Optional.empty(); // Return empty Optional if no clients are found
        }
        List<ClientDTO> clientDTOs = clients.stream()
                .map(ClientDTO::new).toList();
        return Optional.of(clientDTOs);
    }

    /**
     * Метод для получения клиента по его id
     *
     * @param id клиента в базе
     * @return возвращает клиента
     */
    @Override
    public Optional<ClientDTO> findClientById(long id) {

        return clientRepository.findById(id)
                .map(ClientDTO::new);
    }

    /**
     * Метод для получения клиента по его телефонному номеру
     *
     * @param phoneNumber телефонный номер клиента
     * @return возвращает клиента
     */
    @Override
    public Optional<ClientDTO> findClientByPhoneNumber(String phoneNumber) {
        if (phoneNumber.startsWith("8") || phoneNumber.startsWith("7")) {
            phoneNumber = "+7" + phoneNumber.substring(1);
        }
        return clientRepository.findByPhoneNumber(phoneNumber)
                .map(ClientDTO::new);
    }

    /**
     * Метод для получения клиента по его UserName в телеграмме
     *
     * @param telegramUserName клиента
     * @return возвращает клиента
     */
    @Override
    public Optional<ClientDTO> findClientByTelegramUser(String telegramUserName) {
        return clientRepository.findByTelegramUser(telegramUserName)
                .map(ClientDTO::new);
    }

    /**
     * Метод для получения клиента по ИД чата в телеграмме
     *
     * @param chatID клиента
     * @return возвращает клиента
     */
    @Override
    public Optional<ClientDTO> findClientByTelegramChat(String chatID) {
        return clientRepository.findByTelegramChat(chatID)
                .map(ClientDTO::new);
    }

    /**
     * Метод для обновления данных клиента
     *
     * @param id клиента
     * @param clientDetails DTO клиента
     * @return возвращает обновленного клиента
     */
    public ResponseEntity<ClientDTO> updateClient(Long id, ClientDTO clientDetails) {

        Optional<Client> clientOptional = clientRepository.findById(id);
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            client.setFirstName(clientDetails.getFirstName());
            client.setLastName(clientDetails.getLastName());
            client.setPatronymic(clientDetails.getPatronymic());
            client.setBirthday(clientDetails.getBirthday());
            client.setGender(Gender.valueOf(clientDetails.getGender()));
            client.setWallet(clientDetails.getWallet());
            client.setPhoneNumber(clientDetails.getPhoneNumber());
            client.setTelegramUser(clientDetails.getTelegramUser());
            client.setTelegramChat(clientDetails.getTelegramChat());
            client = saveClient(client);

            return ResponseEntity.ok(new ClientDTO(client));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Метод для сохранения нового клиента
     *
     * @param client объект клиент
     * @return возвращает сохраненного клиента
     */
    @Override
    public Client saveClient(Client client) {
        clientRepository.save(client);
        return client;
    }

    /**
     * Метод для удаления клиента из БД
     *
     * @param id клиента
     * @return возвращает удаленного клиента
     */
    @Override
    public Optional<Client> deleteClient(Long id) {

        Optional<Client> client = clientRepository.findById(id);
        if (client.isPresent()) {
            clientRepository.delete(client.get());
        }
        return client;
    }

    /**
     * Метод для сохранения клиента в базу по его ДТО
     *
     * @param clientDTO ДТО клиента
     * @return возвращает сохраненного клиента
     */
    public Client saveClientDto(ClientDTO clientDTO) {
        Client client = new Client();
        client.setId(clientDTO.getId());
        client.setFirstName(clientDTO.getFirstName());
        client.setLastName(clientDTO.getLastName());
        client.setPatronymic(clientDTO.getPatronymic());
        client.setBirthday(clientDTO.getBirthday());
        client.setGender(Gender.valueOf(clientDTO.getGender()));
        client.setWallet(clientDTO.getWallet());
        client.setPhoneNumber(clientDTO.getPhoneNumber());
        client.setTelegramUser(clientDTO.getTelegramUser());
        client.setTelegramChat(clientDTO.getTelegramChat());
        return clientRepository.save(client);

    }

}
