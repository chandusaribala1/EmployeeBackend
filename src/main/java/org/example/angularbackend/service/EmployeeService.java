package org.example.angularbackend.service;

import java.util.List;
import java.util.Locale;
import org.example.angularbackend.dto.EmployeeResponse;
import org.example.angularbackend.dto.LoginRequest;
import org.example.angularbackend.dto.RegisterRequest;
import org.example.angularbackend.entity.Employee;
import org.example.angularbackend.repository.AttendanceRepository;
import org.example.angularbackend.repository.EmployeeRepository;
import org.example.angularbackend.repository.LeaveRequestRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository, AttendanceRepository attendanceRepository,
                          LeaveRequestRepository leaveRequestRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.attendanceRepository = attendanceRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public EmployeeResponse register(RegisterRequest request) {
        String employeeId = normalizeId(request.employeeId());
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (employeeRepository.existsById(employeeId)) {
            throw new ApiException(HttpStatus.CONFLICT, "Employee ID is already registered.");
        }
        if (employeeRepository.existsByEmailIgnoreCase(email)) {
            throw new ApiException(HttpStatus.CONFLICT, "Email address is already registered.");
        }
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        employee.setFirstName(request.firstName().trim());
        employee.setLastName(request.lastName().trim());
        employee.setDesignation(request.designation().trim());
        employee.setEmail(email);
        employee.setPasswordHash(passwordEncoder.encode(request.password()));
        return toResponse(employeeRepository.save(employee));
    }

    @Transactional(readOnly = true)
    public EmployeeResponse login(LoginRequest request) {
        Employee employee = employeeRepository.findByEmployeeIdIgnoreCase(normalizeId(request.employeeId()))
                .filter(item -> passwordEncoder.matches(request.password(), item.getPasswordHash()))
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Employee ID or password was not accepted."));
        return toResponse(employee);
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> findAll() {
        return employeeRepository.findAll().stream()
                .sorted((left, right) -> left.getLastName().compareToIgnoreCase(right.getLastName()))
                .map(EmployeeService::toResponse).toList();
    }

    @Transactional
    public void delete(String employeeId) {
        Employee employee = employeeRepository.findByEmployeeIdIgnoreCase(normalizeId(employeeId))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Employee was not found."));
        attendanceRepository.deleteForEmployee(employee.getEmployeeId());
        leaveRequestRepository.deleteForEmployee(employee.getEmployeeId());
        employeeRepository.delete(employee);
    }

    private static String normalizeId(String id) {
        return id.trim().toUpperCase(Locale.ROOT);
    }

    private static EmployeeResponse toResponse(Employee employee) {
        return new EmployeeResponse(employee.getEmployeeId(), employee.getFirstName(), employee.getLastName(),
                employee.getDesignation(), employee.getEmail());
    }
}
