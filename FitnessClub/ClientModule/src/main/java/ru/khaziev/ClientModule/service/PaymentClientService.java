package ru.khaziev.ClientModule.service;

import org.springframework.http.ResponseEntity;
import ru.khaziev.ClientModule.entity.Payment;

import java.util.List;

public interface PaymentClientService {
    public ResponseEntity<List<Payment>> findAllPayments();
    public ResponseEntity<Payment> findPaymentById(Long id);
    public ResponseEntity<List<Payment>> findPaymentsByClient(Long id);
    public Payment savePayment(Payment payment);
    public void deletePayment(Long id);
    public Payment updatePayment(Long id,Payment payment);
    //public PaymentDTO convertToDTO(Long id);
}
