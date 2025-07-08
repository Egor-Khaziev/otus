package ru.khaziev.ClientModule.entity;

import jakarta.persistence.*;
import ru.khaziev.ClientModule.enums.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clients_seq")
    @SequenceGenerator(name = "clients_seq", sequenceName = "clients_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "patronymic", length = 100)
    private String patronymic;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "wallet", columnDefinition = "DECIMAL(10, 2) DEFAULT 0") // Changed to BigDecimal, columnDefinition added
    private BigDecimal wallet; // Changed to BigDecimal

    @Column(name = "phone_number", length = 100)
    private String phoneNumber;

    @Column(name = "telegram", length = 100)
    private String telegramUser;

    @Column(name = "telegram_id", length = 100)
    private String telegramChat;



    public Client() {
    }

    public Client(Gender gender, LocalDate birthday, String firstName, String patronymic, String lastName, BigDecimal wallet, String phoneNumber, String telegramUser, String telegramChat) {
        this.gender = gender;
        this.birthday = birthday;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.lastName = lastName;
        this.wallet = wallet;
        this.phoneNumber = phoneNumber;
        this.telegramUser = telegramUser;
        this.telegramChat = telegramChat;
    }

    public Long getId() {
        return id;
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getLastName() {
        return lastName;
    }

    public BigDecimal getWallet() {
        return wallet;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getTelegramUser() {
        return telegramUser;
    }

    public String getTelegramChat() {
        return telegramChat;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setWallet(BigDecimal wallet) {
        this.wallet = wallet;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setTelegramUser(String telegramUser) {
        this.telegramUser = telegramUser;
    }

    public void setTelegramChat(String telegramChat) {
        this.telegramChat = telegramChat;
    }
}