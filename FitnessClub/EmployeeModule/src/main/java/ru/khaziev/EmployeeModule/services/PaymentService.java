package ru.khaziev.EmployeeModule.services;

import ru.khaziev.EmployeeModule.DTO.PaymentDTO;
import java.util.List;

public interface PaymentService {
    PaymentDTO createPayment(PaymentDTO PaymentDTO);
    PaymentDTO getPaymentById(Long id);
    List<PaymentDTO> getAllPayments();
    PaymentDTO updatePayment(PaymentDTO PaymentDTO);
    void deletePayment(Long id);
}