package com.example.employeemanagement.repository;

import com.example.employeemanagement.model.Employee;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository {
    List<Employee> findAll();
    List<Employee> findAll(int page, int size, String sortField);
    long count();
    Optional<Employee> findById(Long id);
    Employee save(Employee employee);
    void deleteById(Long id);
    boolean existsByEmail(String email);
    boolean existsById(Long id);
}
