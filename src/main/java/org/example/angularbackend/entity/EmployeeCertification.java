package org.example.angularbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employee_certifications", schema = "public2")
@Getter
@Setter
@NoArgsConstructor
public class EmployeeCertification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false, length = 32)
    private String employeeId;

    @Column(name = "certification_name", nullable = false, length = 180)
    private String certificationName;

    public EmployeeCertification(String employeeId, String certificationName) {
        this.employeeId = employeeId;
        this.certificationName = certificationName;
    }
}
