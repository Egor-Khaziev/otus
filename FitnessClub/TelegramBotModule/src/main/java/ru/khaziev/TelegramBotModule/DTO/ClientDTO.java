package ru.khaziev.TelegramBotModule.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

//@Getter
//@Setter
//@NoArgsConstructor
public class ClientDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private LocalDate birthday;
    private String gender;
    private BigDecimal wallet;
    private String phoneNumber;
    private String telegramUser;
    private String telegramChat;

    @Override
    public String toString() {
        return "ClientDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthday=" + birthday +
                ", gender='" + gender + '\'' +
                ", wallet=" + wallet +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", telegram='" + telegramUser + '\'' +
                ", chat='" + telegramChat + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public BigDecimal getWallet() {
        return wallet;
    }

    public void setWallet(BigDecimal wallet) {
        this.wallet = wallet;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTelegramUser() {
        return telegramUser;
    }

    public void setTelegramUser(String telegramUser) {
        this.telegramUser = telegramUser;
    }

    public String getTelegramChat() {
        return telegramChat;
    }

    public void setTelegramChat(String telegramChat) {
        this.telegramChat = telegramChat;
    }

    public ClientDTO() {
    }
}