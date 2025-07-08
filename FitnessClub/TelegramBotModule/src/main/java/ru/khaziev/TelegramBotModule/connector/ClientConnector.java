package ru.khaziev.TelegramBotModule.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.observation.annotation.Observed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.khaziev.TelegramBotModule.DTO.ClientDTO;

import java.io.IOException;

@Component
@Observed(name = "telegram.connector")
public class ClientConnector {

//    private final String baseUrl = "http://gateway:8080";
    private final String baseUrl = "http://localhost:8080";
    @Autowired
    private  HttpRequest connector;
    @Autowired
    private  ObjectMapper objectMapper;


    public ClientConnector() {
//    public ClientConnector(String baseUrl) {
//        this.baseUrl = baseUrl;
//        this.connector = new HttpRequest();
//        this.objectMapper = new ObjectMapper();
//        this.objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.configOverride(LocalDate.class)
//                .setFormat(com.fasterxml.jackson.annotation.JsonFormat.Value.forPattern("yyyy-MM-dd"));
    }

    @Observed(name = "getClient",
            contextualName = "get-client-by-id",
            lowCardinalityKeyValues = {"apiVersion", "v1", "clientType", "telegram"})
    public ClientDTO getClient(Long id) throws IOException {
        String apiUrl = baseUrl + "/api/clients/" + id;
        String response = connector.sendGetRequest(apiUrl,null);

        if (response != null) {
            return objectMapper.readValue(response, ClientDTO.class);
        } else {
            return null;
        }
    }

    @Observed(name = "getClientByTelegramUser",
            contextualName = "get-client-by-telegram-userName",
            lowCardinalityKeyValues = {"apiVersion", "v1", "clientType", "telegram"})
    public ClientDTO getClientByTelegramUser(String telegramUserName) throws IOException {
        String apiUrl = baseUrl + "/api/clients/telegram/name:" + telegramUserName;
        String response = connector.sendGetRequest(apiUrl,null);

        if (response != null) {
            return objectMapper.readValue(response, ClientDTO.class);
        } else {
            return null;
        }
    }

    @Observed(name = "getClientByTelegramChat",
            contextualName = "get-ChatId",
            lowCardinalityKeyValues = {"apiVersion", "v1", "clientType", "telegram"})
    public ClientDTO getClientByTelegramChat(String ChatId) throws IOException {
        String apiUrl = baseUrl + "/api/clients/telegram/chat:" + ChatId;
        String response = connector.sendGetRequest(apiUrl,null);

        if (response != null) {
            return objectMapper.readValue(response, ClientDTO.class);
        } else {
            return null;
        }
    }

    @Observed(name = "getClientByPhone",
            contextualName = "get-phoneNumber",
            lowCardinalityKeyValues = {"apiVersion", "v1", "clientType", "telegram"})
    public ClientDTO getClientByPhone(String phoneNumber) throws IOException {
        String apiUrl = baseUrl + "/api/clients/phone/" + phoneNumber;
        String response = connector.sendGetRequest(apiUrl,null);

        if (response != null) {
            return objectMapper.readValue(response, ClientDTO.class);
        } else {
            return null;
        }
    }

    @Observed(name = "getAllClients",
            contextualName = "get-all-clients-after-start",
            lowCardinalityKeyValues = {"apiVersion", "v1", "clientType", "telegram"})
    public ClientDTO[] getAllClients() throws IOException {
        String apiUrl = baseUrl + "/api/clients";
        String response = connector.sendGetRequest(apiUrl, null);

        if (response != null) {
            return objectMapper.readValue(response, ClientDTO[].class);
        } else {
            return null;
        }
    }

    public ClientDTO createClient(ClientDTO clientDTO) throws IOException {
        String apiUrl = baseUrl + "/api/clients";
        String response = connector.sendPostRequest(apiUrl, clientDTO,null);

        if (response != null) {
            return objectMapper.readValue(response, ClientDTO.class);
        } else {
            return null;
        }
    }

    @Observed(name = "updateClient",
            contextualName = "update-client-after-check",
            lowCardinalityKeyValues = {"apiVersion", "v1", "clientType", "telegram"})
    public ClientDTO updateClient(Long id, ClientDTO clientDTO) throws IOException {
        String apiUrl = baseUrl + "/api/clients/" + id;
        String response = connector.sendPutRequest(apiUrl, clientDTO, null);

        if (response != null) {
            return objectMapper.readValue(response, ClientDTO.class);
        } else {
            return null;
        }
    }

    public boolean deleteClient(Long id) throws IOException {
        String apiUrl = baseUrl + "/api/clients/" + id;
        String response = connector.sendDeleteRequest(apiUrl, null);
        return response != null;
    }

    /*
    public static void main(String[] args) {
        String baseUrl = "http://localhost:8081";
        ClientConnector clientConnector = new ClientConnector(baseUrl);

        try {
            // Create
            ClientDTO newClientDTO = new ClientDTO();
            newClientDTO.setFirstName("Bob");
            newClientDTO.setLastName("Doe");
            newClientDTO.setPatronymic("Middle");
            newClientDTO.setBirthday(LocalDate.of(1990, 1, 1));
            newClientDTO.setGender("MAN");
            newClientDTO.setWallet(new BigDecimal("100.50"));
            newClientDTO.setPhoneNumber("+79161234055");
            newClientDTO.setTelegram("@johndoe");
            System.out.println("befor created client: " + newClientDTO);


            ClientDTO createdClientDTO = clientConnector.createClient(newClientDTO);
            System.out.println("After created client: " + createdClientDTO);

            // Get by id
            if (createdClientDTO != null) {
                ClientDTO retrievedClientDTO = clientConnector.getClient(createdClientDTO.getId());
                System.out.println("Retrieved client: " + retrievedClientDTO);

                // Update
                retrievedClientDTO.setPhoneNumber("+79100030000");
                ClientDTO updatedClientDTO = clientConnector.updateClient(retrievedClientDTO.getId(), retrievedClientDTO);
                System.out.println("Updated client: " + updatedClientDTO);

                // Get All
                ClientDTO[] allClientDTOs = clientConnector.getAllClients();
                System.out.println("All clients:");
                if (allClientDTOs != null) {  // Check for null before iterating
                    for (ClientDTO clientDTO : allClientDTOs) {
                        System.out.println(clientDTO);
                    }
                } else {
                    System.out.println("No clients found.");
                }


                // Delete
                boolean deleted = clientConnector.deleteClient(retrievedClientDTO.getId());
                System.out.println("Client deleted: " + deleted);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     */
}