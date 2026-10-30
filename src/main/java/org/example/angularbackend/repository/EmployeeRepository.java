package org.example.angularbackend.repository;

import java.util.Optional;
import org.example.angularbackend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
    boolean existsByEmailIgnoreCase(String email);
    Optional<Employee> findByEmployeeIdIgnoreCase(String employeeId);
}
