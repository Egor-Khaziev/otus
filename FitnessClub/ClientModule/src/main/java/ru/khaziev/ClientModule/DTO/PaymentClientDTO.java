package ru.khaziev.ClientModule.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.khaziev.ClientModule.entity.Payment;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PaymentClientDTO {

    private Long operationId;
    private String operationPublicId;
    private String operationName;
    private BigDecimal operationAmount;
    private String operationType; // String representation of the enum
    private Long clientId; // Assuming you'll just send the client ID

    public PaymentClientDTO(Payment payment) {
        this.operationId = payment.getOperationId();
        this.operationPublicId = payment.getOperationPublicId();
        this.operationName = payment.getOperationName();
        this.operationAmount = payment.getOperationAmount();
        this.operationType = payment.getOperationType().toString();
        this.clientId = payment.getClient().getId();
    }
}
