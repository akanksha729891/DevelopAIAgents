package com.example.employeemanagement.dto;

import com.example.employeemanagement.model.Payment.PaymentType;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
public class PaymentDto {
    @NotNull
    private Long employeeId;
    
    @NotNull
    @Positive(message = "Payment amount must be positive")
    private Double amount;
    
    private String description;
    
    @NotNull
    private PaymentType type;
    
    private LocalDateTime paymentDate;
}
