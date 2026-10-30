package org.example.angularbackend.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record EmployeeProfileRequest(
        @NotBlank @Size(max = 120) String fatherName,
        @NotBlank @Size(max = 120) String motherName,
        @NotBlank @Size(max = 4000) String permanentAddress,
        @NotNull @Past LocalDate dateOfBirth,
        @Size(max = 4_194_304) String profileImageBase64,
        @NotNull @DecimalMin("0") @DecimalMax("100") BigDecimal tenthMarks,
        @NotBlank @Pattern(regexp = "12th|Diploma") String intermediateType,
        @NotNull @DecimalMin("0") @DecimalMax("100") BigDecimal intermediateMarks,
        @NotBlank @Size(max = 120) String educationGroup,
        @NotBlank @Size(max = 120) String graduationQualification,
        @Size(max = 160) String graduationSpecialization,
        @Size(max = 120) String graduationGroup,
        @Size(max = 120) String postgraduateQualification,
        @Size(max = 160) String postgraduateSpecialization,
        @Pattern(regexp = "^$|[0-9]{12}") String aadhaarNumber,
        @Pattern(regexp = "(?i)^$|[A-Z]{5}[0-9]{4}[A-Z]") String panNumber,
        @NotNull @Size(max = 50) List<@NotBlank @Size(max = 100) String> skills,
        @NotNull @Size(max = 100) List<@NotBlank @Size(max = 180) String> certifications) { }
