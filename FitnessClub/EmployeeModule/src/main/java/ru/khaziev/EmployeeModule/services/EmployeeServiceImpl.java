package ru.khaziev.EmployeeModule.services;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.khaziev.EmployeeModule.DTO.EmployeeDTO;
import ru.khaziev.EmployeeModule.entity.Employee;
import ru.khaziev.EmployeeModule.enums.Gender;
import ru.khaziev.EmployeeModule.repository.EmployeeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements ru.khaziev.EmployeeModule.services.EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final ConcurrentSkipListMap<Long, CachedEmployee> employeeCache = new ConcurrentSkipListMap<>();
    private final ScheduledExecutorService cacheCleanupScheduler = Executors.newScheduledThreadPool(1);
    private final long cacheExpirationTimeMinutes = 10; // Cache expiration time in minutes
    private final int maxCacheSize = 1000;

    @PostConstruct
    public void initialize() {
        cacheCleanupScheduler.scheduleAtFixedRate(this::cleanupCache, 1, 1, TimeUnit.MINUTES);
    }

    @PreDestroy
    public void destroy() {
        cacheCleanupScheduler.shutdown();
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = convertToEntity(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        employeeCache.put(savedEmployee.getId(), new CachedEmployee(savedEmployee));
        return convertToDTO(savedEmployee);
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        CachedEmployee cachedEmployee = employeeCache.get(id);
        if (cachedEmployee != null) {
            cachedEmployee.setLastAccessTime(LocalDateTime.now()); // Update access time
            return convertToDTO(cachedEmployee.getEmployee());
        }
        EmployeeDTO employeeDTO = employeeRepository.findById(id)
                                    .map(this::convertToDTO).orElse(null);
        if (employeeDTO !=null) {
            if (employeeCache.size() >= maxCacheSize) {
                // если переполнен удаляем самую старую запись
                Long firstKey = employeeCache.firstKey();
                employeeCache.remove(firstKey);
            }
            employeeCache.put(id, new CachedEmployee(convertToEntity(employeeDTO)));
        }
        return employeeDTO;
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        employeeCache.replace(id, new CachedEmployee(convertToEntity(employeeDTO)));
        return employeeRepository.findById(id)
                .map(existingEmployee -> {
                    existingEmployee.setFirstName(employeeDTO.getFirstName());
                    existingEmployee.setLastName(employeeDTO.getLastName());
                    existingEmployee.setPatronymic(employeeDTO.getPatronymic());
                    existingEmployee.setGender(Gender.valueOf(employeeDTO.getGender())); // Convert string to enum
                    existingEmployee.setBirthday(employeeDTO.getBirthday());
                    existingEmployee.setSalary(employeeDTO.getSalary());
                    Employee updatedEmployee = employeeRepository.save(existingEmployee);
                    return convertToDTO(updatedEmployee);
                })
                .orElse(null);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeCache.remove(id);
        employeeRepository.deleteById(id);
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setPatronymic(employee.getPatronymic());
        employeeDTO.setGender(employee.getGender().toString()); // Convert enum to string
        employeeDTO.setBirthday(employee.getBirthday());
        employeeDTO.setSalary(employee.getSalary());
        return employeeDTO;
    }

    private Employee convertToEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setId(employeeDTO.getId()); // You might need to handle null id in create
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setPatronymic(employeeDTO.getPatronymic());
        employee.setGender(Gender.valueOf(employeeDTO.getGender()));  // Convert string to enum
        employee.setBirthday(employeeDTO.getBirthday());
        employee.setSalary(employeeDTO.getSalary());
        return employee;
    }

    private void cleanupCache() {
        LocalDateTime now = LocalDateTime.now();
        employeeCache.entrySet().removeIf(entry -> entry.getValue().getLastAccessTime().plusMinutes(cacheExpirationTimeMinutes).isBefore(now));
        System.out.println("Cache cleaned up. Current size: " + employeeCache.size());
    }

    // Вспомогательный клас для хранения Сотрудников + время
    private static class CachedEmployee implements Comparable<CachedEmployee> {
        private final Employee employee ;
        private LocalDateTime lastAccessTime;

        public CachedEmployee(Employee employee) {
            this.employee = employee;
            this.lastAccessTime = LocalDateTime.now();
        }

        public Employee getEmployee() {
            return employee;
        }

        public LocalDateTime getLastAccessTime() {
            return lastAccessTime;
        }

        public void setLastAccessTime(LocalDateTime lastAccessTime) {
            this.lastAccessTime = lastAccessTime;
        }

        @Override
        public int compareTo(CachedEmployee other) {
            return this.lastAccessTime.compareTo(other.lastAccessTime);
        }
    }
}