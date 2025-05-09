package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.PaymentDto;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.model.Payment;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Payment> getAllPayments(int page, int size, String sortBy) {
        return paymentRepository.findAll(page, size, sortBy);
    }
    
    public long countPayments() {
        return paymentRepository.count();
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));
    }

    public List<Payment> getPaymentsByEmployeeId(Long employeeId) {
        // Verify employee exists
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }
        return paymentRepository.findByEmployeeId(employeeId);
    }

    public Payment createPayment(PaymentDto paymentDto) {
        // Verify employee exists
        Employee employee = employeeRepository.findById(paymentDto.getEmployeeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
        
        Payment payment = new Payment();
        updatePaymentFromDto(payment, paymentDto);
        
        // Set initial status
        payment.setStatus(Payment.PaymentStatus.PENDING);
        
        return paymentRepository.save(payment);
    }

    public Payment processPayment(Long id) {
        Payment payment = getPaymentById(id);
        
        // In a real application, this would involve payment gateway integration
        // For this demo, we'll simply mark it as completed
        payment.setStatus(Payment.PaymentStatus.COMPLETED);
        
        return paymentRepository.save(payment);
    }

    public Payment cancelPayment(Long id) {
        Payment payment = getPaymentById(id);
        
        // Only pending payments can be cancelled
        if (payment.getStatus() != Payment.PaymentStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Only pending payments can be cancelled");
        }
        
        payment.setStatus(Payment.PaymentStatus.CANCELLED);
        
        return paymentRepository.save(payment);
    }

    public void deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found");
        }
        paymentRepository.deleteById(id);
    }

    private void updatePaymentFromDto(Payment payment, PaymentDto dto) {
        payment.setEmployeeId(dto.getEmployeeId());
        payment.setAmount(dto.getAmount());
        payment.setDescription(dto.getDescription());
        payment.setType(dto.getType());
        
        // Set payment date if provided, otherwise it will be set to now in onCreate()
        if (dto.getPaymentDate() != null) {
            payment.setPaymentDate(dto.getPaymentDate());
        }
    }

    // Method to calculate total payments for an employee
    public Double calculateTotalPaymentsForEmployee(Long employeeId) {
        return getPaymentsByEmployeeId(employeeId).stream()
                .filter(payment -> payment.getStatus() == Payment.PaymentStatus.COMPLETED)
                .mapToDouble(Payment::getAmount)
                .sum();
    }

    // Method to process salary payments for all employees
    public List<Payment> processMonthlySalaries() {
        List<Employee> employees = employeeRepository.findAll();
        
        for (Employee employee : employees) {
            if (employee.getSalary() != null && employee.getSalary() > 0) {
                PaymentDto paymentDto = new PaymentDto();
                paymentDto.setEmployeeId(employee.getId());
                paymentDto.setAmount(employee.getSalary());
                paymentDto.setType(Payment.PaymentType.SALARY);
                paymentDto.setDescription("Monthly salary payment");
                
                Payment payment = createPayment(paymentDto);
                processPayment(payment.getId());
            }
        }
        
        return paymentRepository.findAll();
    }
}
