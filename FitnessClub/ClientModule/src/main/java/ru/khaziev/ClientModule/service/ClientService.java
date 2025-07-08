package ru.khaziev.ClientModule.service;

import org.springframework.http.ResponseEntity;
import ru.khaziev.ClientModule.DTO.ClientDTO;
import ru.khaziev.ClientModule.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    Optional<List<ClientDTO>> findAllClients();
    Optional<ClientDTO> findClientById(long id);
    Client saveClient(Client client);
    Optional<Client> deleteClient(Long id);
    Optional<ClientDTO> findClientByPhoneNumber(String phoneNumber);
    Optional<ClientDTO> findClientByTelegramUser(String telegramUserName);
    Optional<ClientDTO> findClientByTelegramChat(String chatID);
    ResponseEntity<ClientDTO> updateClient(Long id, ClientDTO clientDetails);
    Client saveClientDto(ClientDTO clientDTO);

}
