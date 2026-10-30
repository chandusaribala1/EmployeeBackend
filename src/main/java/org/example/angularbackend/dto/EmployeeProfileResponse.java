package org.example.angularbackend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record EmployeeProfileResponse(
        String employeeId,
        String fatherName,
        String motherName,
        String permanentAddress,
        LocalDate dateOfBirth,
        String profileImageBase64,
        BigDecimal tenthMarks,
        String intermediateType,
        BigDecimal intermediateMarks,
        String educationGroup,
        String graduationQualification,
        String graduationSpecialization,
        String graduationGroup,
        String postgraduateQualification,
        String postgraduateSpecialization,
        String aadhaarNumber,
        String panNumber,
        List<String> skills,
        List<String> certifications) { }
