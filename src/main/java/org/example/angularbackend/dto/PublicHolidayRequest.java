package org.example.angularbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record PublicHolidayRequest(
        @NotNull LocalDate date,
        @NotBlank @Size(max = 120) String name,
        @Size(max = 500) String description,
        @NotBlank String employeeId) { }
