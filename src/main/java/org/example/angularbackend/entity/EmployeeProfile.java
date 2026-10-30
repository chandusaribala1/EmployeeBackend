package org.example.angularbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employee_profiles", schema = "public2")
@Getter
@Setter
@NoArgsConstructor
public class EmployeeProfile {
    @Id
    @Column(name = "employee_id", length = 32)
    private String employeeId;

    @Column(nullable = false, length = 120)
    private String fatherName;

    @Column(nullable = false, length = 120)
    private String motherName;

    @Column(nullable = false, columnDefinition = "text")
    private String permanentAddress;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "profile_image")
    private byte[] profileImage;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal tenthMarks;

    @Column(nullable = false, length = 12)
    private String intermediateType;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal intermediateMarks;

    @Column(name = "education_group", nullable = false, length = 120)
    private String educationGroup;

    @Column(nullable = false, length = 120)
    private String graduationQualification;

    @Column(length = 160)
    private String graduationSpecialization;

    @Column(length = 120)
    private String graduationGroup;

    @Column(length = 120)
    private String postgraduateQualification;

    @Column(length = 160)
    private String postgraduateSpecialization;

    @Column(name = "aadhaar_number", length = 12)
    private String aadhaarNumber;

    @Column(name = "pan_number", length = 10)
    private String panNumber;

    @Column(nullable = false)
    private Instant updatedAt;
}
