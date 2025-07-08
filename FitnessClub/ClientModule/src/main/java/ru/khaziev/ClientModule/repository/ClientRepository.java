package ru.khaziev.ClientModule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khaziev.ClientModule.entity.Client;

import java.nio.channels.FileChannel;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByPhoneNumber(String phoneNumber);
    Optional<Client> findByTelegramUser(String telegram);

    Optional<Client> findByTelegramChat(String chatID);
}
