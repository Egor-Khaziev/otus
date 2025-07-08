package ru.khaziev.ClientModule.DTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.khaziev.ClientModule.entity.Client;
import ru.khaziev.ClientModule.entity.Payment;
import ru.khaziev.ClientModule.enums.Gender;
import ru.khaziev.ClientModule.enums.OperationTypeClient;
import ru.khaziev.ClientModule.service.ClientService;

@Component
public class ConvertToEntity {

    @Autowired
    ClientService clientService;

    public Client convert(ClientDTO clientDTO) {
        Client client = new Client();
        client.setId(clientDTO
                .getId());
        client.setGender(Gender.valueOf(clientDTO.getGender()));
        client.setFirstName(clientDTO.getFirstName());
        client.setLastName(clientDTO.getLastName());
        client.setPatronymic(clientDTO.getPatronymic());
        client.setBirthday(clientDTO.getBirthday());
        client.setWallet(clientDTO.getWallet());
        client.setPhoneNumber(clientDTO.getPhoneNumber());
        client.setTelegramUser(clientDTO.getTelegramUser());
        client.setTelegramChat(clientDTO.getTelegramChat());

        return client;
    }

    public Payment convert(PaymentClientDTO paymentClientDTO) {

        ClientDTO client = clientService.findClientById(paymentClientDTO.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client with ID " + paymentClientDTO.getClientId() + " not found"));

        Payment payment = new Payment();
        payment.setOperationId(paymentClientDTO.getOperationId());
        payment.setOperationAmount(paymentClientDTO.getOperationAmount());
        payment.setOperationId(paymentClientDTO.getOperationId());
        payment.setOperationPublicId(paymentClientDTO.getOperationPublicId());
        payment.setOperationType(OperationTypeClient.valueOf(paymentClientDTO.getOperationType()));
        payment.setOperationName(paymentClientDTO.getOperationName());
        payment.setClient(convert(client));

        return payment;
    }
}
