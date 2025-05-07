package com.example.employeemanagement.repository;

import com.example.employeemanagement.model.Employee;
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
public class InMemoryEmployeeRepository implements EmployeeRepository {
    private final Map<Long, Employee> employees = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(employees.values());
    }
    
    @Override
    public List<Employee> findAll(int page, int size, String sortField) {
        Comparator<Employee> comparator;
        
        switch (sortField) {
            case "firstName":
                comparator = Comparator.comparing(Employee::getFirstName);
                break;
            case "lastName":
                comparator = Comparator.comparing(Employee::getLastName);
                break;
            case "email":
                comparator = Comparator.comparing(Employee::getEmail);
                break;
            case "position":
                comparator = Comparator.comparing(Employee::getPosition, 
                    Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "salary":
                comparator = Comparator.comparing(Employee::getSalary, 
                    Comparator.nullsLast(Comparator.naturalOrder()));
                break;
            case "createdAt":
                comparator = Comparator.comparing(Employee::getCreatedAt);
                break;
            default:
                comparator = Comparator.comparing(Employee::getId);
        }
        
        return employees.values().stream()
                .sorted(comparator)
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }
    
    @Override
    public long count() {
        return employees.size();
    }

    @Override
    public Optional<Employee> findById(Long id) {
        return Optional.ofNullable(employees.get(id));
    }

    @Override
    public Employee save(Employee employee) {
        if (employee.getId() == null) {
            employee.setId(idCounter.getAndIncrement());
            employee.onCreate();
        }
        employees.put(employee.getId(), employee);
        return employee;
    }

    @Override
    public void deleteById(Long id) {
        employees.remove(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return employees.values().stream()
                .anyMatch(employee -> employee.getEmail().equals(email));
    }
    
    @Override
    public boolean existsById(Long id) {
        return employees.containsKey(id);
    }
}