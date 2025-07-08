package ru.khaziev.EmployeeModule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.khaziev.EmployeeModule.entity.Payment;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
}