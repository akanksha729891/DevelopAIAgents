package com.example.employeemanagement.repository;

import com.example.employeemanagement.model.Payment;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository {
    List<Payment> findAll();
    List<Payment> findAll(int page, int size, String sortField);
    List<Payment> findByEmployeeId(Long employeeId);
    Optional<Payment> findById(Long id);
    Payment save(Payment payment);
    void deleteById(Long id);
    boolean existsById(Long id);
    long count();
}
