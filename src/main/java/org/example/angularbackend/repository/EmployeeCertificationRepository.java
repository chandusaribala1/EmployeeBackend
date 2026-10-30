package org.example.angularbackend.repository;

import java.util.List;
import org.example.angularbackend.entity.EmployeeCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeCertificationRepository extends JpaRepository<EmployeeCertification, Long> {
    List<EmployeeCertification> findAllByEmployeeIdOrderByCertificationNameAsc(String employeeId);

    @Modifying
    @Query("delete from EmployeeCertification c where c.employeeId = :employeeId")
    void deleteForEmployee(@Param("employeeId") String employeeId);
}
