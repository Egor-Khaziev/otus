package ru.khaziev.ClientModule.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.khaziev.ClientModule.DTO.PaymentClientDTO;
import ru.khaziev.ClientModule.entity.Payment;
import ru.khaziev.ClientModule.service.PaymentClientService;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Платежи", description = "Операции, связанные с платежами") // Тег для группировки endpoints
public class PaymentClientController {

    private final PaymentClientService paymentClientService;

    @Autowired
    public PaymentClientController(PaymentClientService paymentClientService) {
        this.paymentClientService = paymentClientService;
    }

    @Operation(summary = "Создать новый платеж", description = "Создает новый платеж.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Платеж создан",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentClientDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос",
                    content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentClientDTO> createPayment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Объект платежа, который нужно создать", required = true,
                    content = @Content(schema = @Schema(implementation = Payment.class)))
            @RequestBody Payment payment) {
        try {
            Payment createdPayment = paymentClientService.savePayment(payment);
            return new ResponseEntity<>(new PaymentClientDTO(createdPayment), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Или вернуть более информативное сообщение об ошибке
        }
    }

    @Operation(summary = "Найти платежи по ID клиента", description = "Возвращает список платежей, связанных с указанным клиентом.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список платежей",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class))}),
            @ApiResponse(responseCode = "404", description = "Платежи не найдены",
                    content = @Content)
    })
    @GetMapping("/client/{id}")
    public ResponseEntity<List<Payment>> findPaymentsByClient(
            @Parameter(description = "ID клиента, для которого нужно найти платежи", required = true)
            @PathVariable Long id) {
        ResponseEntity<List<Payment>> listPayments = paymentClientService.findPaymentsByClient(id);
        return listPayments;
    }

    @Operation(summary = "Получить платеж по ID", description = "Извлекает платеж по его уникальному ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Платеж найден",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class))}),
            @ApiResponse(responseCode = "404", description = "Платеж не найден",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(
            @Parameter(description = "ID платежа, которого нужно получить", required = true)
            @PathVariable Long id) {
        return paymentClientService.findPaymentById(id);
    }

    @Operation(summary = "Получить все платежи", description = "Извлекает список всех платежей.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список платежей",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class))}),
            @ApiResponse(responseCode = "404", description = "Платежи не найдены",
                    content = @Content)
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Payment>> getAllPayments() {
        return paymentClientService.findAllPayments();
    }

    @Operation(summary = "Обновить существующий платеж", description = "Обновляет существующий платеж по его ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Платеж обновлен",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class))}),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Платеж не найден",
                    content = @Content)
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Payment> updatePayment(
            @Parameter(description = "ID платежа, которого нужно обновить", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Объект платежа с обновленными данными", required = true,
                    content = @Content(schema = @Schema(implementation = Payment.class)))
            @RequestBody Payment paymentDetails) {
        try {
            Payment updatedPayment = paymentClientService.updatePayment(id, paymentDetails);
            return ResponseEntity.ok(updatedPayment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Или вернуть более информативное сообщение об ошибке
        }
    }

    @Operation(summary = "Удалить платеж", description = "Удаляет платеж по его ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Платеж удален",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Платеж не найден",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(
            @Parameter(description = "ID платежа, которого нужно удалить", required = true)
            @PathVariable Long id) {
        try {
            paymentClientService.deletePayment(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}