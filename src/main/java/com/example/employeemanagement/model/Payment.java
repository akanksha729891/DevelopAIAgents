package com.example.employeemanagement.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Payment {
    private Long id;
    private Long employeeId;
    private Double amount;
    private String description;
    private PaymentStatus status;
    private PaymentType type;
    private LocalDateTime paymentDate;
    private LocalDateTime createdAt;
    
    public enum PaymentStatus {
        PENDING,
        COMPLETED,
        FAILED,
        CANCELLED
    }
    
    public enum PaymentType {
        SALARY,
        BONUS,
        REIMBURSEMENT,
        ADVANCE,
        OTHER
    }
    
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (paymentDate == null) {
            paymentDate = LocalDateTime.now();
        }
        if (status == null) {
            status = PaymentStatus.PENDING;
        }
    }
}
