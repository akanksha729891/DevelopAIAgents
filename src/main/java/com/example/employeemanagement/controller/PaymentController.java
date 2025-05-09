package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.PaymentDto;
import com.example.employeemanagement.model.Payment;
import com.example.employeemanagement.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        
        List<Payment> payments = paymentService.getAllPayments(page, size, sortBy);
        long totalItems = paymentService.countPayments();
        
        Map<String, Object> response = new HashMap<>();
        response.put("payments", payments);
        response.put("totalItems", totalItems);
        response.put("currentPage", page);
        response.put("totalPages", (int) Math.ceil((double) totalItems / size));
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Payment>> getPaymentsByEmployeeId(@PathVariable Long employeeId) {
        return ResponseEntity.ok(paymentService.getPaymentsByEmployeeId(employeeId));
    }

    @GetMapping("/employee/{employeeId}/total")
    public ResponseEntity<Map<String, Double>> getTotalPaymentsForEmployee(@PathVariable Long employeeId) {
        Double total = paymentService.calculateTotalPaymentsForEmployee(employeeId);
        Map<String, Double> response = new HashMap<>();
        response.put("totalPayments", total);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody PaymentDto paymentDto) {
        return ResponseEntity.ok(paymentService.createPayment(paymentDto));
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<Payment> processPayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.processPayment(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Payment> cancelPayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.cancelPayment(id));
    }

    @PostMapping("/process-monthly-salaries")
    public ResponseEntity<List<Payment>> processMonthlySalaries() {
        return ResponseEntity.ok(paymentService.processMonthlySalaries());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok().build();
    }
}
