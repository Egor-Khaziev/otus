package ru.khaziev.EmployeeModule.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.khaziev.EmployeeModule.DTO.PaymentDTO;
import ru.khaziev.EmployeeModule.entity.Payment;
import ru.khaziev.EmployeeModule.enums.OperationType;
import ru.khaziev.EmployeeModule.repository.EmployeeRepository;
import ru.khaziev.EmployeeModule.repository.PaymentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private final PaymentRepository paymentRepository;
    @Autowired
    private final EmployeeRepository employeeRepository;

    @Override
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        Payment payment = convertToEntity(paymentDTO);
        Payment savedPayment = paymentRepository.save(payment);
        return convertToDTO(savedPayment);
    }

    @Override
    public PaymentDTO getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDTO updatePayment(PaymentDTO paymentDTO) {

        //return convertToDTO(paymentRepository.findById(id).get());

        return paymentRepository.findById(paymentDTO.getOperationId())
                .map(existingPayment -> {
                    existingPayment.setOperationPublicId(paymentDTO.getOperationPublicId());
                    existingPayment.setOperationName(paymentDTO.getOperationName());
                    existingPayment.setOperationAmount(paymentDTO.getOperationAmount());
                    existingPayment.setOperationType(OperationType.valueOf(paymentDTO.getOperationType()));
                    // TODO добавить обработку Exception если null
                    existingPayment.setEmployee(employeeRepository.findById(paymentDTO.getEmployeeId()).get()); // Implement the logic to set employee
                    Payment updatedPayment = paymentRepository.save(existingPayment);
                    return convertToDTO(updatedPayment);
                })
                .orElse(null);
    }

    @Override
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setOperationId(payment.getOperationId());
        paymentDTO.setOperationPublicId(payment.getOperationPublicId());
        paymentDTO.setOperationName(payment.getOperationName());
        paymentDTO.setOperationAmount(payment.getOperationAmount());
        paymentDTO.setOperationType(payment.getOperationType().toString()); // Convert enum to string
        if (payment.getEmployee() != null) {
            paymentDTO.setEmployeeId(payment.getEmployee().getId());
        }
        return paymentDTO;
    }

    private Payment convertToEntity(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setOperationId(paymentDTO.getOperationId());
        payment.setOperationPublicId(paymentDTO.getOperationPublicId());
        payment.setOperationName(paymentDTO.getOperationName());
        payment.setOperationAmount(paymentDTO.getOperationAmount());
        payment.setOperationType(OperationType.valueOf(paymentDTO.getOperationType())); // Convert string to enum
        // TODO добавить обработку Exception если null
        payment.setEmployee(employeeRepository.findById(paymentDTO.getEmployeeId()).get()); // Implement the logic to set employee
        return payment;
    }
}