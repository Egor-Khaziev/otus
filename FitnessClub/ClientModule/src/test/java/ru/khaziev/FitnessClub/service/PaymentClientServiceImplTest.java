package ru.khaziev.FitnessClub.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.khaziev.ClientModule.entity.Client;
import ru.khaziev.ClientModule.entity.Payment;
import ru.khaziev.ClientModule.enums.OperationTypeClient;
import ru.khaziev.ClientModule.repository.ClientRepository;
import ru.khaziev.ClientModule.repository.PaymentClientRepository;
import ru.khaziev.ClientModule.service.PaymentClientServiceImpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PaymentClientRepository paymentClientRepository;

    @InjectMocks
    private PaymentClientServiceImpl paymentClientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSavePayment_whenClientExistsAndValidPayment_shouldSavePayment() {
        // Arrange
        Client client = new Client();
        client.setId(1L);
        client.setWallet(BigDecimal.valueOf(100)); // Initial wallet
        Payment payment = new Payment();
        payment.setClient(client);
        payment.setOperationAmount(BigDecimal.valueOf(20));
        payment.setOperationType(OperationTypeClient.PURCHASE);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(paymentClientRepository.save(any(Payment.class))).thenReturn(payment);

        // Act
        Payment savedPayment = paymentClientService.savePayment(payment);

        // Assert
        assertNotNull(savedPayment);
        assertEquals(BigDecimal.valueOf(80), client.getWallet()); // Check wallet
        verify(clientRepository, times(1)).findById(1L);
        verify(paymentClientRepository, times(1)).save(payment);
        verify(clientRepository, times(1)).save(client); // Ensure client is saved
    }

    @Test
    void testSavePayment_whenClientDoesNotExist_shouldThrowException() {
        // Arrange
        Payment payment = new Payment();
        Client client = new Client();
        client.setId(1L);
        payment.setClient(client);

        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentClientService.savePayment(payment);
        });

        assertEquals("Client not found with id: 1", exception.getMessage());
        verify(clientRepository, times(1)).findById(1L);
        verify(paymentClientRepository, never()).save(any(Payment.class));
    }

    @Test
    void testSavePayment_whenInvalidOperationAmount_shouldThrowException() {
        // Arrange
        Client client = new Client();
        client.setId(1L);
        client.setWallet(BigDecimal.valueOf(100));
        Payment payment = new Payment();
        payment.setClient(client);
        payment.setOperationAmount(BigDecimal.valueOf(-10)); // Invalid amount
        payment.setOperationType(OperationTypeClient.PURCHASE);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentClientService.savePayment(payment);
        });

        assertEquals("Operation amount must be a positive value.", exception.getMessage());
        verify(clientRepository, times(1)).findById(1L);
        verify(paymentClientRepository, never()).save(any(Payment.class));
    }

    @Test
    void testFindPaymentById_whenPaymentExists_shouldReturnPayment() {
        // Arrange
        Long paymentId = 1L;
        Payment payment = new Payment();
        payment.setOperationId(paymentId);
        when(paymentClientRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        // Act
        ResponseEntity<Payment> response = paymentClientService.findPaymentById(paymentId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(paymentId, response.getBody().getOperationId());
        verify(paymentClientRepository, times(1)).findById(paymentId);
    }

    @Test
    void testFindPaymentById_whenPaymentDoesNotExist_shouldReturnNotFound() {
        // Arrange
        Long paymentId = 1L;
        when(paymentClientRepository.findById(paymentId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Payment> response = paymentClientService.findPaymentById(paymentId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(paymentClientRepository, times(1)).findById(paymentId);
    }

    @Test
    void testFindPaymentsByClient_whenClientExistsAndPaymentsFound_shouldReturnPayments() {
        // Arrange
        Client client = new Client();
        client.setId(1L);
        Payment payment1 = new Payment();
        payment1.setClient(client);
        Payment payment2 = new Payment();
        payment2.setClient(client);
        List<Payment> payments = Arrays.asList(payment1, payment2);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(paymentClientRepository.findPaymentsByClient(client)).thenReturn(Optional.of(payments));

        // Act
        ResponseEntity<List<Payment>> response = paymentClientService.findPaymentsByClient(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(clientRepository, times(1)).findById(1L);
        verify(paymentClientRepository, times(1)).findPaymentsByClient(client);
    }

    @Test
    void testFindPaymentsByClient_whenClientExistsButNoPaymentsFound_shouldReturnNotFound() {
        // Arrange
        Client client = new Client();
        client.setId(1L);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(paymentClientRepository.findPaymentsByClient(client)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<List<Payment>> response = paymentClientService.findPaymentsByClient(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(clientRepository, times(1)).findById(1L);
        verify(paymentClientRepository, times(1)).findPaymentsByClient(client);
    }

    @Test
    void testFindPaymentsByClient_whenClientDoesNotExist_shouldThrowException() {
        // Arrange
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentClientService.findPaymentsByClient(1L);
        });

        assertEquals("Client not found with id: 1", exception.getMessage());
        verify(clientRepository, times(1)).findById(1L);
        verify(paymentClientRepository, never()).findPaymentsByClient(any());
    }

    @Test
    void testFindAllPayments_whenPaymentsExist_shouldReturnPayments() {
        // Arrange
        Payment payment1 = new Payment();
        Payment payment2 = new Payment();
        List<Payment> payments = Arrays.asList(payment1, payment2);
        when(paymentClientRepository.findAll()).thenReturn(payments);

        // Act
        ResponseEntity<List<Payment>> response = paymentClientService.findAllPayments();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(paymentClientRepository, times(1)).findAll();
    }

    @Test
    void testUpdatePayment_whenPaymentExistsAndSufficientFunds_shouldUpdatePayment() {
        // Arrange
        Long paymentId = 1L;
        Client client = new Client();
        client.setId(1L);
        client.setWallet(BigDecimal.valueOf(100));

        Payment existingPayment = new Payment();
        existingPayment.setOperationId(paymentId);
        existingPayment.setClient(client);
        existingPayment.setOperationAmount(BigDecimal.valueOf(20));
        existingPayment.setOperationType(OperationTypeClient.PURCHASE);

        Payment paymentDetails = new Payment();
        paymentDetails.setOperationAmount(BigDecimal.valueOf(30)); // Update to 30
        paymentDetails.setOperationType(OperationTypeClient.PURCHASE); // Keep same
        paymentDetails.setClient(client);

        when(paymentClientRepository.findById(paymentId)).thenReturn(Optional.of(existingPayment));
        when(paymentClientRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return saved payment

        // Act
        Payment updatedPayment = paymentClientService.updatePayment(paymentId, paymentDetails);

        // Assert
        assertNotNull(updatedPayment);
        assertEquals(BigDecimal.valueOf(70), client.getWallet()); // Wallet updated
        verify(paymentClientRepository, times(1)).findById(paymentId);
        verify(paymentClientRepository, times(1)).save(any(Payment.class));
        verify(clientRepository, times(1)).save(client); // Ensure client is saved
    }

    @Test
    void testUpdatePayment_whenPaymentExistsButInsufficientFunds_shouldThrowException() {
        // Arrange
        Long paymentId = 1L;
        Client client = new Client();
        client.setId(1L);
        client.setWallet(BigDecimal.valueOf(10)); // Only 10 in wallet

        Payment existingPayment = new Payment();
        existingPayment.setOperationId(paymentId);
        existingPayment.setClient(client);
        existingPayment.setOperationAmount(BigDecimal.valueOf(20));
        existingPayment.setOperationType(OperationTypeClient.PURCHASE);

        Payment paymentDetails = new Payment();
        paymentDetails.setOperationAmount(BigDecimal.valueOf(30)); // Try to purchase 30
        paymentDetails.setOperationType(OperationTypeClient.PURCHASE);
        paymentDetails.setClient(client);

        when(paymentClientRepository.findById(paymentId)).thenReturn(Optional.of(existingPayment));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentClientService.updatePayment(paymentId, paymentDetails);
        });

        assertEquals("Insufficient funds for purchase.", exception.getMessage());
        verify(paymentClientRepository, times(1)).findById(paymentId);
        verify(paymentClientRepository, never()).save(any(Payment.class));
        verify(clientRepository, never()).save(client); // Ensure client is NOT saved
    }


    @Test
    void testDeletePayment_whenPaymentExists_shouldDeletePaymentAndReverseOperation() {
        // Arrange
        Long paymentId = 1L;
        Client client = new Client();
        client.setId(1L);
        client.setWallet(BigDecimal.valueOf(100));
        Payment payment = new Payment();
        payment.setOperationId(paymentId);
        payment.setClient(client);
        payment.setOperationAmount(BigDecimal.valueOf(20));
        payment.setOperationType(OperationTypeClient.PURCHASE);

        when(paymentClientRepository.existsById(paymentId)).thenReturn(true);
        when(paymentClientRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        doNothing().when(paymentClientRepository).deleteById(paymentId);
        // Настраиваем мок для clientRepository.save, чтобы вернуть обновленного клиента
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> {
            Client savedClient = invocation.getArgument(0);
            return savedClient;
        });
        // Act
        paymentClientService.deletePayment(paymentId);

        // Assert
        assertEquals(BigDecimal.valueOf(120), client.getWallet()); // Reverted
        verify(paymentClientRepository, times(1)).existsById(paymentId);
        verify(paymentClientRepository, times(1)).findById(paymentId);  // Изменили количество вызовов findById
        verify(paymentClientRepository, times(1)).deleteById(paymentId);
        verify(clientRepository, times(1)).save(client); // Ensure client is saved
    }
    @Test
    void testDeletePayment_whenPaymentDoesNotExist_shouldThrowException() {
        // Arrange
        Long paymentId = 1L;
        when(paymentClientRepository.existsById(paymentId)).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentClientService.deletePayment(paymentId);
        });

        assertEquals("Payment not found with id: 1", exception.getMessage());
        verify(paymentClientRepository, times(1)).existsById(paymentId);
        verify(paymentClientRepository, never()).findById(any());
        verify(paymentClientRepository, never()).deleteById(any());
        verify(clientRepository, never()).save(any());
    }
}