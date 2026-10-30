package org.example.angularbackend.controller;

import jakarta.validation.Valid;
import org.example.angularbackend.dto.EmployeeProfileRequest;
import org.example.angularbackend.dto.EmployeeProfileResponse;
import org.example.angularbackend.service.EmployeeProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees/{employeeId}/profile")
public class EmployeeProfileController {
    private final EmployeeProfileService employeeProfileService;

    public EmployeeProfileController(EmployeeProfileService employeeProfileService) {
        this.employeeProfileService = employeeProfileService;
    }

    @GetMapping
    public EmployeeProfileResponse get(@PathVariable String employeeId) { return employeeProfileService.get(employeeId); }

    @PutMapping
    public EmployeeProfileResponse save(@PathVariable String employeeId, @Valid @RequestBody EmployeeProfileRequest request) {
        return employeeProfileService.save(employeeId, request);
    }
}
