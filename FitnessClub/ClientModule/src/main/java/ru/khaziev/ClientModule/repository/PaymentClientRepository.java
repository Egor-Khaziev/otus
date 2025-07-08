package ru.khaziev.ClientModule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khaziev.ClientModule.entity.Client;
import ru.khaziev.ClientModule.entity.Payment;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentClientRepository extends JpaRepository<Payment, Long> {
    Optional<List<Payment>> findPaymentsByClient(Client client);
}
