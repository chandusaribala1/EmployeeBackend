package org.example.angularbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employees", schema = "public2", uniqueConstraints = @UniqueConstraint(name = "uk_employee_email", columnNames = "email"))
@Getter
@Setter
@NoArgsConstructor
public class Employee {
    @Id
    @Column(name = "employee_id", nullable = false, updatable = false, length = 32)
    private String employeeId;

    @Column(nullable = false, length = 80)
    private String firstName;

    @Column(nullable = false, length = 80)
    private String lastName;

    @Column(nullable = false, length = 120)
    private String designation;

    @Column(nullable = false, length = 180)
    private String email;

    @Column(nullable = false, name = "password_hash", length = 100)
    private String passwordHash;
}
