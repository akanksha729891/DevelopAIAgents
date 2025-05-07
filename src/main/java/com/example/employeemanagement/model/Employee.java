package com.example.employeemanagement.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String position;
    private Double salary;
    private LocalDateTime createdAt;
    
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
