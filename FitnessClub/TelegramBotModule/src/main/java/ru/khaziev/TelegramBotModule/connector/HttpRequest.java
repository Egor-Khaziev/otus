package ru.khaziev.TelegramBotModule.connector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class HttpRequest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public String sendGetRequest(String apiUrl, Map<String, String> headers) throws IOException {
        return sendRequest(apiUrl, "GET", null, headers);
    }

    public String sendPostRequest(String apiUrl, Object requestBody, Map<String, String> headers) throws IOException {
        return sendRequest(apiUrl, "POST", requestBody, headers);
    }

    public String sendPutRequest(String apiUrl, Object requestBody, Map<String, String> headers) throws IOException {
        return sendRequest(apiUrl, "PUT", requestBody, headers);
    }

    public String sendDeleteRequest(String apiUrl, Map<String, String> headers) throws IOException {
        return sendRequest(apiUrl, "DELETE", null, headers);
    }

    private String sendRequest(String apiUrl, String requestMethod, Object requestBody, Map<String, String> headers) throws IOException {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            try {
                connection.setRequestMethod(requestMethod);

                // headers
                if (headers != null) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        connection.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }
                connection.setRequestProperty("Content-Type", "application/json");

                if (!"GET".equalsIgnoreCase(requestMethod) && !"DELETE".equalsIgnoreCase(requestMethod) && requestBody != null) {
                    connection.setDoOutput(true);
                    String jsonRequestBody = objectMapper.writeValueAsString(requestBody);
                    /// debug
//                    System.out.println(jsonRequestBody);

                    byte[] input = jsonRequestBody.getBytes(StandardCharsets.UTF_8);
                    connection.setRequestProperty("Content-Length", String.valueOf(input.length));

                    try (OutputStream os = connection.getOutputStream()) {
                        os.write(input, 0, input.length);
                    }
                }

                int responseCode = connection.getResponseCode();



                if (responseCode >= 200 && responseCode < 300) {
                    //response
                    System.out.println("Response Code: " + responseCode + " for " + requestMethod + " " + apiUrl);
                    try (java.io.InputStream in = connection.getInputStream();
                         java.util.Scanner scanner = new java.util.Scanner(in, StandardCharsets.UTF_8)) {
                        return scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                    }
                } else {
                    //response
                    System.err.println("Error Response Code: " + responseCode + " for " + requestMethod + " " + apiUrl);
                    /*try (java.io.InputStream errorStream = connection.getErrorStream();
                         java.util.Scanner scanner = new java.util.Scanner(errorStream, StandardCharsets.UTF_8)) {
                        if (scanner.hasNext()) {
                            System.err.println("Error Response Body: " + scanner.useDelimiter("\\A").next());
                        }
                    }*/
                    return null;
                }
            } finally {
                connection.disconnect();
            }
        } catch (IOException e) {
            System.err.println("IOException during " + requestMethod + " request to " + apiUrl + ": " + e.getMessage());
            throw e;
        }
    }
    /*
    public static void main(String[] args) {
        HttpRequest httpRequest = new HttpRequest();
        String apiUrl = "http://localhost:8081/api/clients";
        //Map<String, String> headers = Map.of("Authorization", "Bearer token");

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setFirstName("John");
        clientDTO.setLastName("Doe");
        clientDTO.setPatronymic("Middle");
        clientDTO.setBirthday(LocalDate.of(1990, 1, 1));
        clientDTO.setGender("MAN");
        clientDTO.setWallet(new BigDecimal("100.50"));
        clientDTO.setPhoneNumber("+79161234595");
        clientDTO.setTelegram("@jonsdoe");

        try {
            //String postResponse = httpRequest.sendPostRequest(apiUrl, Map.of("key", "value"), headers);
            String postResponse = httpRequest.sendPostRequest(apiUrl, clientDTO, null);
            System.out.println("POST Response: " + postResponse);

        } catch (IOException e) {
            System.err.println("Error in main: " + e.getMessage());
        }
    }
    */
}