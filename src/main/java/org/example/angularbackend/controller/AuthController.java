package org.example.angularbackend.controller;

import jakarta.validation.Valid;
import org.example.angularbackend.dto.EmployeeResponse;
import org.example.angularbackend.dto.LoginRequest;
import org.example.angularbackend.dto.RegisterRequest;
import org.example.angularbackend.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final EmployeeService employeeService;

    public AuthController(EmployeeService employeeService) { this.employeeService = employeeService; }

    @PostMapping("/login")
    public EmployeeResponse login(@Valid @RequestBody LoginRequest request) { return employeeService.login(request); }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse register(@Valid @RequestBody RegisterRequest request) { return employeeService.register(request); }
}
