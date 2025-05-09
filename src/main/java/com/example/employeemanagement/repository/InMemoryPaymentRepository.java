package com.example.employeemanagement.repository;

import com.example.employeemanagement.model.Payment;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryPaymentRepository implements PaymentRepository {
    private final Map<Long, Payment> payments = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public List<Payment> findAll() {
        return new ArrayList<>(payments.values());
    }
    
    @Override
    public List<Payment> findAll(int page, int size, String sortField) {
        Comparator<Payment> comparator;
        
        switch (sortField) {
            case "employeeId":
                comparator = Comparator.comparing(Payment::getEmployeeId);
                break;
            case "amount":
                comparator = Comparator.comparing(Payment::getAmount);
                break;
            case "status":
                comparator = Comparator.comparing(payment -> payment.getStatus().name());
                break;
            case "type":
                comparator = Comparator.comparing(payment -> payment.getType().name());
                break;
            case "paymentDate":
                comparator = Comparator.comparing(Payment::getPaymentDate);
                break;
            case "createdAt":
                comparator = Comparator.comparing(Payment::getCreatedAt);
                break;
            default:
                comparator = Comparator.comparing(Payment::getId);
        }
        
        return payments.values().stream()
                .sorted(comparator)
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Payment> findByEmployeeId(Long employeeId) {
        return payments.values().stream()
                .filter(payment -> payment.getEmployeeId().equals(employeeId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return Optional.ofNullable(payments.get(id));
    }

    @Override
    public Payment save(Payment payment) {
        if (payment.getId() == null) {
            payment.setId(idCounter.getAndIncrement());
            payment.onCreate();
        }
        payments.put(payment.getId(), payment);
        return payment;
    }

    @Override
    public void deleteById(Long id) {
        payments.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return payments.containsKey(id);
    }
    
    @Override
    public long count() {
        return payments.size();
    }
}
