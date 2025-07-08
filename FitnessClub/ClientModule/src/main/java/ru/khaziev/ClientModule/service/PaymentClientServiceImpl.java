package ru.khaziev.ClientModule.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.khaziev.ClientModule.entity.Client;
import ru.khaziev.ClientModule.entity.Payment;
import ru.khaziev.ClientModule.enums.OperationTypeClient;
import ru.khaziev.ClientModule.repository.ClientRepository;
import ru.khaziev.ClientModule.repository.PaymentClientRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Класс Сервис для работы с начислениями и расчетами клиентов
 *
 * @author Khegori
 * @version 1.0
 */
@Service
public class PaymentClientServiceImpl implements PaymentClientService {

    private final ClientRepository clientRepository;
    private final PaymentClientRepository paymentClientRepository;


    @Autowired
    public PaymentClientServiceImpl(ClientRepository clientRepository, PaymentClientRepository paymentClientRepository) {
        this.clientRepository = clientRepository;
        this.paymentClientRepository = paymentClientRepository;
    }

    /**
     * Сохранение финансовой операции расчет или поступление
     *
     * @param payment денежная операция
     *                меньше 0 - расчет
     *                больше 0 - зачисление
     */
    @Override
    @Transactional
    public Payment savePayment(Payment payment) {
        // Basic validation: Client must exist
        Client client = clientRepository.findById(payment.getClient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + payment.getClient().getId()));

        // Additional validation if needed
        if (payment.getOperationAmount() == null || payment.getOperationAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Operation amount must be a positive value.");
        }

        // Business logic: Apply the payment (e.g., update client wallet)
        applyPayment(payment);

        return paymentClientRepository.save(payment);
    }

    /**
     * Поиск финансовой операции по ее id
     *
     * @param id ИД операции в базе
     * @return возвращает финансовую операцию
     */
    @Override
    public ResponseEntity<Payment>  findPaymentById(Long id) {
        Optional<Payment> optionalPayment = paymentClientRepository.findById(id);
        return optionalPayment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Поиск списка операций по клиенту
     *
     * @param id ИД клиента в базе
     * @return Лист финансовых операций клиента
     */
    @Override
    public ResponseEntity<List<Payment>> findPaymentsByClient(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + id));
        Optional<List<Payment>> optionalClientPayments = paymentClientRepository.findPaymentsByClient(client);
        return optionalClientPayments.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Получения списка всех операций
     *
     * @return лист всех операций
     */
    @Override
    public ResponseEntity<List<Payment>> findAllPayments() {
        List<Payment> payments =  paymentClientRepository.findAll();
        return ResponseEntity.ok(payments);
    }

    /**
     * Изменение финансовой операции по ИД
     *
     * @param id финансовой операции для изменения
     * @param paymentDetails объект Payment (финансовая операция) измененный для сохранения
     * @return объект Payment (финансовая операция) после изменения
     */
    @Override
    @Transactional
    public Payment updatePayment(Long id, Payment paymentDetails) {
        Payment existingPayment = paymentClientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with id: " + id));

        // Update fields (consider which fields are updatable)
        existingPayment.setOperationPublicId(paymentDetails.getOperationPublicId());
        existingPayment.setOperationName(paymentDetails.getOperationName());
        existingPayment.setOperationAmount(paymentDetails.getOperationAmount());
        existingPayment.setOperationType(paymentDetails.getOperationType());
        existingPayment.setClient(paymentDetails.getClient());

        // Re-apply payment after changes
        applyPayment(existingPayment);
        return paymentClientRepository.save(existingPayment);
    }

    /**
     * Удаление финансовой операции из базы
     *
     * @param id ИД финансовой операции на удаление
     */
    @Override
    @Transactional
    public void deletePayment(Long id) {
        if (!paymentClientRepository.existsById(id)) {
            throw new IllegalArgumentException("Payment not found with id: " + id);
        }
        //Откат операции в кошельке
        Payment payment = paymentClientRepository.findById(id).get();
        OperationTypeClient reverseOperationTypeClient = payment.getOperationType().equals(OperationTypeClient.PURCHASE) ? OperationTypeClient.DEPOSIT : OperationTypeClient.PURCHASE;
        payment.setOperationType(reverseOperationTypeClient);
        applyPayment(payment);

        paymentClientRepository.deleteById(id);

    }

    /**
     * Изменение баланса Клиента в соответствии с финансовой операцией
     *
     * @param payment финансовая операция
     */
    private void applyPayment(Payment payment) {
        Client client = payment.getClient();
        BigDecimal amount = payment.getOperationAmount();
        if (payment.getOperationType() == OperationTypeClient.DEPOSIT) {
            client.setWallet(client.getWallet().add(amount));
        } else if (payment.getOperationType() == OperationTypeClient.PURCHASE) {
            if (client.getWallet().compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient funds for purchase.");
            }
            client.setWallet(client.getWallet().subtract(amount));
        }
        clientRepository.save(client); // Save the updated client
    }

}