package ru.khaziev.FitnessClub.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import ru.khaziev.ClientModule.DTO.ClientDTO;
import ru.khaziev.ClientModule.entity.Client;
import ru.khaziev.ClientModule.enums.Gender;
import ru.khaziev.ClientModule.repository.ClientRepository;
import ru.khaziev.ClientModule.service.ClientServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testFindAllClients_whenClientsExist_shouldReturnClientDTOs() {
        // Arrange (Подготовка)
        Client client1 = new Client();
        client1.setId(1L);
        client1.setFirstName("John");
        client1.setLastName("Doe");
        client1.setGender(Gender.MAN);
        client1.setPhoneNumber("+79123456789");
        Client client2 = new Client();
        client2.setId(2L);
        client2.setFirstName("Jane");
        client2.setLastName("Smith");
        client2.setGender(Gender.WOMAN);
        client2.setPhoneNumber("+79876543210");

        List<Client> clients = List.of(client1, client2);
        when(clientRepository.findAll()).thenReturn(clients);

        // Act (Действие)
        Optional<List<ClientDTO>> result = clientService.findAllClients();

        // Assert (Проверка)
        assertTrue(result.isPresent());
        List<ClientDTO> clientDTOs = result.get();
        assertEquals(2, clientDTOs.size());
        assertEquals("John", clientDTOs.get(0).getFirstName());
        assertEquals("Jane", clientDTOs.get(1).getFirstName());

        verify(clientRepository, times(1)).findAll(); // Verify that findAll() was called once
    }

    @Test
    void testFindAllClients_whenNoClientsExist_shouldReturnEmptyOptional() {
        // Arrange
        when(clientRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        Optional<List<ClientDTO>> result = clientService.findAllClients();

        // Assert
        assertFalse(result.isPresent());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testFindClientById_whenClientExists_shouldReturnClientDTO() {
        // Arrange
        long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);
        client.setFirstName("John");
        client.setGender(Gender.MAN);
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        // Act
        Optional<ClientDTO> result = clientService.findClientById(clientId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(clientRepository, times(1)).findById(clientId);
    }

    @Test
    void testFindClientById_whenClientDoesNotExist_shouldReturnEmptyOptional() {
        // Arrange
        long clientId = 1L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // Act
        Optional<ClientDTO> result = clientService.findClientById(clientId);

        // Assert
        assertFalse(result.isPresent());
        verify(clientRepository, times(1)).findById(clientId);
    }

    @Test
    void testFindClientByPhoneNumber_whenPhoneNumberExistsWithPlus7Prefix_shouldReturnClientDTO() {
        // Arrange
        String phoneNumber = "+79123456789";
        Client client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setGender(Gender.MAN);
        when(clientRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(client));

        // Act
        Optional<ClientDTO> result = clientService.findClientByPhoneNumber(phoneNumber);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(clientRepository, times(1)).findByPhoneNumber(phoneNumber);
    }

    @Test
    void testFindClientByPhoneNumber_whenPhoneNumberExistsWith7Prefix_shouldReturnClientDTO() {
        // Arrange
        String phoneNumber = "79123456789";
        String expectedPhoneNumber = "+79123456789"; // Проверяем, что префикс "+7" добавлен
        Client client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setGender(Gender.MAN);
        when(clientRepository.findByPhoneNumber(expectedPhoneNumber)).thenReturn(Optional.of(client));

        // Act
        Optional<ClientDTO> result = clientService.findClientByPhoneNumber(phoneNumber);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(clientRepository, times(1)).findByPhoneNumber(expectedPhoneNumber);
    }

    @Test
    void testFindClientByPhoneNumber_whenPhoneNumberExistsWith8Prefix_shouldReturnClientDTO() {
        // Arrange
        String phoneNumber = "89123456789";
        String expectedPhoneNumber = "+79123456789"; // Проверяем, что префикс "+7" добавлен
        Client client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setGender(Gender.MAN);
        when(clientRepository.findByPhoneNumber(expectedPhoneNumber)).thenReturn(Optional.of(client));

        // Act
        Optional<ClientDTO> result = clientService.findClientByPhoneNumber(phoneNumber);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(clientRepository, times(1)).findByPhoneNumber(expectedPhoneNumber);
    }

    @Test
    void testFindClientByPhoneNumber_whenPhoneNumberDoesNotExist_shouldReturnEmptyOptional() {
        // Arrange
        String phoneNumber = "+79123456789";
        when(clientRepository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.empty());

        // Act
        Optional<ClientDTO> result = clientService.findClientByPhoneNumber(phoneNumber);

        // Assert
        assertFalse(result.isPresent());
        verify(clientRepository, times(1)).findByPhoneNumber(phoneNumber);
    }

    @Test
    void testFindClientByTelegramUser_whenTelegramUserExists_shouldReturnClientDTO() {
        // Arrange
        String telegramUser = "@johnDoe";
        Client client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setGender(Gender.MAN);
        when(clientRepository.findByTelegramUser(telegramUser)).thenReturn(Optional.of(client));

        // Act
        Optional<ClientDTO> result = clientService.findClientByTelegramUser(telegramUser);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(clientRepository, times(1)).findByTelegramUser(telegramUser);
    }

    @Test
    void testFindClientByTelegramUser_whenTelegramUserDoesNotExist_shouldReturnEmptyOptional() {
        // Arrange
        String telegramUser = "@johnDoe";
        when(clientRepository.findByTelegramUser(telegramUser)).thenReturn(Optional.empty());

        // Act
        Optional<ClientDTO> result = clientService.findClientByTelegramUser(telegramUser);

        // Assert
        assertFalse(result.isPresent());
        verify(clientRepository, times(1)).findByTelegramUser(telegramUser);
    }

    @Test
    void testFindClientByTelegramChat_whenTelegramChatExists_shouldReturnClientDTO() {
        // Arrange
        String telegramChat = "123456789";
        Client client = new Client();
        client.setId(1L);
        client.setFirstName("John");
        client.setGender(Gender.MAN);
        when(clientRepository.findByTelegramChat(telegramChat)).thenReturn(Optional.of(client));

        // Act
        Optional<ClientDTO> result = clientService.findClientByTelegramChat(telegramChat);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(clientRepository, times(1)).findByTelegramChat(telegramChat);
    }

    @Test
    void testFindClientByTelegramChat_whenTelegramChatDoesNotExist_shouldReturnEmptyOptional() {
        // Arrange
        String telegramChat = "123456789";
        when(clientRepository.findByTelegramChat(telegramChat)).thenReturn(Optional.empty());

        // Act
        Optional<ClientDTO> result = clientService.findClientByTelegramChat(telegramChat);

        // Assert
        assertFalse(result.isPresent());
        verify(clientRepository, times(1)).findByTelegramChat(telegramChat);
    }


    @Test
    void testUpdateClient_whenClientExists_shouldUpdateAndReturnOk() {
        // Arrange
        Long clientId = 1L;
        ClientDTO clientDetails = new ClientDTO();
        clientDetails.setFirstName("UpdatedFirstName");
        clientDetails.setLastName("UpdatedLastName");
        clientDetails.setGender("MAN"); // Assuming Gender is an enum
        clientDetails.setPhoneNumber("+79991112233");

        Client existingClient = new Client();
        existingClient.setId(clientId);
        existingClient.setFirstName("OriginalFirstName");
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Mock save to return the saved client

        // Act
        ResponseEntity<ClientDTO> response = clientService.updateClient(clientId, clientDetails);

        // Assert
        assertEquals(200, response.getStatusCodeValue()); // Check for HTTP OK
        assertNotNull(response.getBody());
        assertEquals("UpdatedFirstName", response.getBody().getFirstName());
        assertEquals("UpdatedLastName", response.getBody().getLastName());
        assertEquals("+79991112233", response.getBody().getPhoneNumber());

        verify(clientRepository, times(1)).findById(clientId);
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void testUpdateClient_whenClientDoesNotExist_shouldReturnNotFound() {
        // Arrange
        Long clientId = 1L;
        ClientDTO clientDetails = new ClientDTO();
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ClientDTO> response = clientService.updateClient(clientId, clientDetails);

        // Assert
        assertEquals(404, response.getStatusCodeValue()); // Check for HTTP Not Found
        verify(clientRepository, times(1)).findById(clientId);
        verify(clientRepository, never()).save(any(Client.class));
    }


    @Test
    void testDeleteClient_whenClientExists_shouldDeleteAndReturnClient() {
        // Arrange
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        doNothing().when(clientRepository).delete(client); // Mock delete

        // Act
        Optional<Client> result = clientService.deleteClient(clientId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(clientId, result.get().getId());
        verify(clientRepository, times(1)).findById(clientId);
        verify(clientRepository, times(1)).delete(client);
    }

    @Test
    void testDeleteClient_whenClientDoesNotExist_shouldReturnEmptyOptional() {
        // Arrange
        Long clientId = 1L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // Act
        Optional<Client> result = clientService.deleteClient(clientId);

        // Assert
        assertFalse(result.isPresent());
        verify(clientRepository, times(1)).findById(clientId);
        verify(clientRepository, never()).delete(any(Client.class));
    }

}