package org.example.angularbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employee_skills", schema = "public2", uniqueConstraints = @UniqueConstraint(name = "uk_employee_skill", columnNames = {"employee_id", "skill_name"}))
@Getter
@Setter
@NoArgsConstructor
public class EmployeeSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false, length = 32)
    private String employeeId;

    @Column(name = "skill_name", nullable = false, length = 100)
    private String skillName;

    public EmployeeSkill(String employeeId, String skillName) {
        this.employeeId = employeeId;
        this.skillName = skillName;
    }
}
