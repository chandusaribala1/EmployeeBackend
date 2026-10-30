package org.example.angularbackend.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;

public record LeaveStatusUpdate(@NotBlank @Pattern(regexp = "Approved|Declined") String status,
                                @NotBlank String reviewerEmployeeId) { }
