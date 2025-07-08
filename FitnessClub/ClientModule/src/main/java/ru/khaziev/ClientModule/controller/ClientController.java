package ru.khaziev.ClientModule.controller;

import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.khaziev.ClientModule.DTO.ClientDTO;
import ru.khaziev.ClientModule.DTO.ConvertToEntity;
import ru.khaziev.ClientModule.entity.Client;
import ru.khaziev.ClientModule.service.ClientServiceImpl;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;


@RestController
@RequestMapping("/api/clients")
@Observed(name = "client.controller")
@Tag(name = "Клиенты", description = "Операции, связанные с клиентами") // Добавьте тег для группировки endpoints
public class ClientController {

    public ClientController() {
        logger = LoggerFactory.getLogger(ClientController.class);;
    }

    @Autowired
    private ClientServiceImpl clientService;
    @Autowired
    private ConvertToEntity convertToEntity;

    private final Logger logger;

    @Operation(summary = "Получить клиента по ID", description = "Извлекает клиента по его уникальному ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клиент найден", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ClientDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Клиент не найден", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClient(
            @Parameter(description = "ID клиента, которого нужно получить", required = true)
            @PathVariable Long id) {
        logger.info("getClient {}", id);
        Optional<ClientDTO> client = clientService.findClientById(id);
        return client.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Получить клиента по номеру телефона", description = "Извлекает клиента по его номеру телефона.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клиент найден", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ClientDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Клиент не найден", content = @Content)
    })
    @GetMapping("/phone/{phoneNumber}")
    @Observed(name = "getClientByPhone",
            contextualName = "get-client-by-phoneNumber",
            lowCardinalityKeyValues = {"apiVersion", "v1", "clientType", "clientModule"})
    public ResponseEntity<ClientDTO> getClientByPhone(
            @Parameter(description = "Номер телефона клиента, которого нужно получить", required = true)
            @PathVariable String phoneNumber) {
        logger.info("getClientByPhone {}", phoneNumber);
        Optional<ClientDTO> client = clientService.findClientByPhoneNumber(phoneNumber);
        return client.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Получить клиента по Telegram username", description = "Извлекает клиента по его Telegram username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клиент найден", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ClientDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Клиент не найден", content = @Content)
    })
    @GetMapping("/telegram/name:{telegramUserName}")
    @Observed(name = "getClientByTelegramUser",
            contextualName = "getClientByTelegramUser",
            lowCardinalityKeyValues = {"apiVersion", "v1", "clientType", "clientModule"})
    public ResponseEntity<ClientDTO> getClientByTelegramUser(
            @Parameter(description = "Telegram username клиента, которого нужно получить", required = true)
            @PathVariable String telegramUserName) {
        logger.info("getClientByTelegramUser {}", telegramUserName);
        Optional<ClientDTO> client = clientService.findClientByTelegramUser(telegramUserName);
        return client.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/telegram/chat:{telegramChat}")
    @Observed(name = "getClientByTelegramChat",
            contextualName = "getClientByTelegramChat",
            lowCardinalityKeyValues = {"apiVersion", "v1", "clientType", "clientModule"})
    public ResponseEntity<ClientDTO> getClientByTelegramChat(
            @Parameter(description = "Telegram chatID клиента, которого нужно получить", required = true)
            @PathVariable String telegramChat) {
        logger.info("getClientByTelegramChat {}", telegramChat);
        Optional<ClientDTO> client = clientService.findClientByTelegramChat(telegramChat);
        return client.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Получить всех клиентов", description = "Извлекает список всех клиентов.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список клиентов", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ClientDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Клиенты не найдены", content = @Content)
    })
    @GetMapping
    @Observed(name = "GetAllClients",
            contextualName = "GetAllClients",
            lowCardinalityKeyValues = {"apiVersion", "v1", "clientType", "clientModule"})
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        logger.info("GetAllClients");
        Optional<List<ClientDTO>> clients = clientService.findAllClients();
        if(clients.isPresent()) {
            return new ResponseEntity<>(clients.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Создать нового клиента", description = "Создает нового клиента.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Клиент создан", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ClientDTO.class))})
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE) // Укажите content type
    public ResponseEntity<ClientDTO> createClient(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Объект клиента, который нужно создать", required = true, content = @Content(schema = @Schema(implementation = ClientDTO.class)))
            @RequestBody ClientDTO client) {
        logger.info("createClient {}", client);
        Client clientEntity = clientService.saveClient(convertToEntity.convert(client));
        return new ResponseEntity<>(new ClientDTO(clientEntity), HttpStatus.CREATED);
    }

    @Operation(summary = "Обновить существующего клиента", description = "Обновляет существующего клиента по его ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клиент обновлен", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ClientDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Клиент не найден", content = @Content)
    })
    @PutMapping("/{id}")
    @Observed(name = "updateClient",
            contextualName = "updateClient",
            lowCardinalityKeyValues = {"apiVersion", "v1", "clientType", "clientModule"})
    public ResponseEntity<ClientDTO> updateClient(
            @Parameter(description = "ID клиента, которого нужно обновить", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Объект клиента с обновленными данными", required = true, content = @Content(schema = @Schema(implementation = ClientDTO.class)))
            @RequestBody ClientDTO client) {
        logger.info("updateClient {}", client);
        return clientService.updateClient(id, client);
    }

    @Operation(summary = "Удалить клиента", description = "Удаляет клиента по его ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клиент удален", content = @Content),
            @ApiResponse(responseCode = "404", description = "Клиент не найден", content = @Content)
    })

    @DeleteMapping("/{id}")
    @Observed(name = "deleteClient",
            contextualName = "deleteClient",
            lowCardinalityKeyValues = {"apiVersion", "v1", "clientType", "clientModule"})
    public ResponseEntity<Void> deleteClient(
            @Parameter(description = "ID клиента, которого нужно удалить", required = true)
            @PathVariable Long id) {
        Optional client = clientService.deleteClient(id);
        if (client.isPresent()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}