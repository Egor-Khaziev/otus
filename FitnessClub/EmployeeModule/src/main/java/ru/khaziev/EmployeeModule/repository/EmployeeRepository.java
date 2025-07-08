package ru.khaziev.EmployeeModule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.khaziev.EmployeeModule.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}