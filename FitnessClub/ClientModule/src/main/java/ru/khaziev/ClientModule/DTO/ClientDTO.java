package ru.khaziev.ClientModule.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.khaziev.ClientModule.entity.Client;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Информация о клиенте")
public class ClientDTO {
    @Schema(description = "Уникальный идентификатор клиента", example = "123")
    private Long id;
    @Schema(description = "Имя клиента", example = "Иван")
    private String firstName;
    @Schema(description = "Фамилия клиента", example = "Иванов")
    private String lastName;
    @Schema(description = "Отчество клиента", example = "Иванович")
    private String patronymic;
    @Schema(description = "Дата рождения клиента", example = "1990-01-01")
    private LocalDate birthday;
    @Schema(description = "Пол клиента", example = "Мужской")
    private String gender;
    @Schema(description = "Баланс кошелька клиента", example = "1000.00")
    private BigDecimal wallet;
    @Schema(description = "Номер телефона клиента", example = "+79991234567")
    private String phoneNumber;
    @Schema(description = "Telegram username клиента", example = "@ivan_ivanov")
    private String telegramUser;
    @Schema(description = "Telegram chatID", example = "440383300")
    private String telegramChat;

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.patronymic = client.getPatronymic();
        this.birthday = client.getBirthday();
        this.gender = client.getGender().toString();
        this.wallet = client.getWallet();
        this.phoneNumber = client.getPhoneNumber();
        this.telegramUser = client.getTelegramUser();
        this.telegramChat = client.getTelegramChat();
    }


}
