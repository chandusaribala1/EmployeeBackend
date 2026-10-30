package org.example.angularbackend.repository;

import java.util.List;
import org.example.angularbackend.entity.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeSkillRepository extends JpaRepository<EmployeeSkill, Long> {
    List<EmployeeSkill> findAllByEmployeeIdOrderBySkillNameAsc(String employeeId);

    @Modifying
    @Query("delete from EmployeeSkill s where s.employeeId = :employeeId")
    void deleteForEmployee(@Param("employeeId") String employeeId);
}
