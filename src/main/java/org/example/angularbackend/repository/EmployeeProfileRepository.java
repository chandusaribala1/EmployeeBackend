package org.example.angularbackend.repository;

import org.example.angularbackend.entity.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, String> { }
