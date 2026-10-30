package org.example.angularbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Pattern(regexp = "[A-Za-z0-9-]{3,32}") String employeeId,
        @NotBlank @Size(max = 80) String firstName,
        @NotBlank @Size(max = 80) String lastName,
        @NotBlank @Size(max = 120) String designation,
        @NotBlank @Email @Size(max = 180) String email,
        @NotBlank @Pattern(regexp = "^[A-Z](?=.*[0-9]).{5,71}$") String password) { }
