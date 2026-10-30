package org.example.angularbackend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.example.angularbackend.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByEmployeeEmployeeIdAndWorkDate(String employeeId, LocalDate workDate);

    List<Attendance> findAllByEmployeeEmployeeIdAndWorkDateBetweenOrderByWorkDateAsc(
            String employeeId, LocalDate startDate, LocalDate endDate);

    @Modifying
    @Query("delete from Attendance a where a.employee.employeeId = :employeeId")
    void deleteForEmployee(@Param("employeeId") String employeeId);
}
