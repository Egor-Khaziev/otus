package ru.khaziev.TelegramBotModule.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.khaziev.TelegramBotModule.DTO.EmployeeDTO;

import java.io.IOException;

@Component
public class EmployeeConnector {

//    private final String baseUrl = "http://gateway:8080";
    private final String baseUrl = "http://localhost:8080";
    @Autowired
    private  HttpRequest connector;
    @Autowired
    private  ObjectMapper objectMapper;
/*
    public EmployeeConnector(String baseUrl) {
        this.baseUrl = baseUrl;
        this.connector = new HttpRequest();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configOverride(LocalDate.class)
                .setFormat(com.fasterxml.jackson.annotation.JsonFormat.Value.forPattern("yyyy-MM-dd"));
    }
*/
    public EmployeeConnector() {}

    public EmployeeDTO getEmployee(Long id) throws IOException {
        String apiUrl = baseUrl + "/api/employees/" + id;
        String response = connector.sendGetRequest(apiUrl, null);

        if (response != null) {
            return objectMapper.readValue(response, EmployeeDTO.class);
        } else {
            return null;
        }
    }

    public EmployeeDTO[] getAllEmployees() throws IOException {
        String apiUrl = baseUrl + "/api/employees";
        String response = connector.sendGetRequest(apiUrl, null);

        if (response != null) {
            return objectMapper.readValue(response, EmployeeDTO[].class);
        } else {
            return null;
        }
    }

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) throws IOException {
        String apiUrl = baseUrl + "/api/employees";
        String response = connector.sendPostRequest(apiUrl, employeeDTO, null);

        if (response != null) {
            return objectMapper.readValue(response, EmployeeDTO.class);
        } else {
            return null;
        }
    }

    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) throws IOException {
        String apiUrl = baseUrl + "/api/employees/" + id;
        String response = connector.sendPutRequest(apiUrl, employeeDTO, null);

        if (response != null) {
            return objectMapper.readValue(response, EmployeeDTO.class);
        } else {
            return null;
        }
    }

    public boolean deleteEmployee(Long id) throws IOException {
        String apiUrl = baseUrl + "/api/employees/" + id;
        String response = connector.sendDeleteRequest(apiUrl, null);
        return response != null;
    }
/*
    public static void main(String[] args) {
        String baseUrl = "http://localhost:8082"; // Replace with your Employee API's base URL
        EmployeeConnector employeeConnector = new EmployeeConnector(baseUrl);

        try {
            // Create
            EmployeeDTO newEmployeeDTO = new EmployeeDTO();
            newEmployeeDTO.setFirstName("Alice");
            newEmployeeDTO.setLastName("Smith");
            newEmployeeDTO.setPatronymic("Middle");
            newEmployeeDTO.setBirthday(LocalDate.of(1990, 1, 1));
            newEmployeeDTO.setGender("MAN");
            newEmployeeDTO.setSalary(new BigDecimal("50000.00"));


            System.out.println("Before creating employee: " + newEmployeeDTO);

            EmployeeDTO createdEmployeeDTO = employeeConnector.createEmployee(newEmployeeDTO);
            System.out.println("After creating employee: " + createdEmployeeDTO);

            // Get by id
            if (createdEmployeeDTO != null) {
                EmployeeDTO retrievedEmployeeDTO = employeeConnector.getEmployee(createdEmployeeDTO.getId());
                System.out.println("Retrieved employee: " + retrievedEmployeeDTO);

                // Update
                retrievedEmployeeDTO.setSalary(new BigDecimal("55000.50"));
                EmployeeDTO updatedEmployeeDTO = employeeConnector.updateEmployee(retrievedEmployeeDTO.getId(), retrievedEmployeeDTO);
                System.out.println("Updated employee: " + updatedEmployeeDTO);

                // Get All
                EmployeeDTO[] allEmployeeDTOs = employeeConnector.getAllEmployees();
                System.out.println("All employees:");
                if (allEmployeeDTOs != null) {  // Check for null before iterating
                    for (EmployeeDTO employeeDTO : allEmployeeDTOs) {
                        System.out.println(employeeDTO);
                    }
                } else {
                    System.out.println("No employees found.");
                }

                // Delete
                boolean deleted = employeeConnector.deleteEmployee(retrievedEmployeeDTO.getId());
                System.out.println("Employee deleted: " + deleted);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

 */
}