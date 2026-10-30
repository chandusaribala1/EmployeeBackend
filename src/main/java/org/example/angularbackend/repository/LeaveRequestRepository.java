package org.example.angularbackend.repository;

import java.util.List;
import org.example.angularbackend.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findAllByOrderByStartDateDesc();

    @Modifying
    @Query("delete from LeaveRequest r where r.employee.employeeId = :employeeId")
    void deleteForEmployee(@Param("employeeId") String employeeId);
}
