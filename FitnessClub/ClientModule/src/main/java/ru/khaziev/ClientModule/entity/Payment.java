package ru.khaziev.ClientModule.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.khaziev.ClientModule.enums.OperationTypeClient;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payments_seq")
    @SequenceGenerator(name = "payments_seq", sequenceName = "payments_seq", allocationSize = 1)
    @Column(name = "operation_id")
    private Long operationId;

    @Column(name = "operation_public_id", length = 200)
    private String operationPublicId;

    @Column(name = "operation_name", length = 200)
    private String operationName;

    @Column(name = "operation_amount")
    private BigDecimal operationAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false)
    private OperationTypeClient operationTypeClient;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public Payment(String operationPublicId, String operationName, BigDecimal operationAmount, OperationTypeClient operationTypeClient, Client client) {
        this.operationPublicId = operationPublicId;
        this.operationName = operationName;
        this.operationAmount = operationAmount;
        this.operationTypeClient = operationTypeClient;
        this.client = client;
    }

    public Payment() {
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public Long getOperationId() {
        return operationId;
    }

    public String getOperationPublicId() {
        return operationPublicId;
    }

    public void setOperationPublicId(String operationPublicId) {
        this.operationPublicId = operationPublicId;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public BigDecimal getOperationAmount() {
        return operationAmount;
    }

    public void setOperationAmount(BigDecimal operationAmount) {
        this.operationAmount = operationAmount;
    }

    public OperationTypeClient getOperationType() {
        return operationTypeClient;
    }

    public void setOperationType(OperationTypeClient operationTypeClient) {
        this.operationTypeClient = operationTypeClient;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}