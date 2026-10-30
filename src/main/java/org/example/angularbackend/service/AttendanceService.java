package org.example.angularbackend.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import org.example.angularbackend.dto.AttendanceResponse;
import org.example.angularbackend.entity.Attendance;
import org.example.angularbackend.entity.Employee;
import org.example.angularbackend.repository.AttendanceRepository;
import org.example.angularbackend.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AttendanceService {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm a", Locale.US);
    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, EmployeeRepository employeeRepository) {
        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> findToday(String employeeId) {
        return attendanceRepository.findByEmployeeEmployeeIdAndWorkDate(normalizeId(employeeId), LocalDate.now())
                .map(AttendanceService::toResponse).map(List::of).orElseGet(List::of);
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> findMonth(String employeeId, int year, int month) {
        java.time.YearMonth requestedMonth;
        try {
            requestedMonth = java.time.YearMonth.of(year, month);
        } catch (java.time.DateTimeException exception) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "A valid year and month are required.");
        }
        String normalizedId = normalizeId(employeeId);
        if (!employeeRepository.findByEmployeeIdIgnoreCase(normalizedId).isPresent()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Employee was not found.");
        }
        return attendanceRepository.findAllByEmployeeEmployeeIdAndWorkDateBetweenOrderByWorkDateAsc(
                        normalizedId, requestedMonth.atDay(1), requestedMonth.atEndOfMonth())
                .stream().map(AttendanceService::toResponse).toList();
    }

    @Transactional
    public AttendanceResponse checkIn(String employeeId) {
        LocalDate today = LocalDate.now();
        Attendance attendance = attendanceRepository.findByEmployeeEmployeeIdAndWorkDate(normalizeId(employeeId), today)
                .orElseGet(() -> newAttendance(employeeId, today));
        if (attendance.getCheckIn() != null) {
            throw new ApiException(HttpStatus.CONFLICT, "Check-in has already been recorded today.");
        }
        LocalTime now = LocalTime.now().withSecond(0).withNano(0);
        attendance.setCheckIn(now);
        attendance.setStatus(now.isAfter(LocalTime.of(9, 0)) ? "Late" : "Present");
        return toResponse(attendanceRepository.save(attendance));
    }

    @Transactional
    public AttendanceResponse checkOut(String employeeId) {
        Attendance attendance = attendanceRepository.findByEmployeeEmployeeIdAndWorkDate(normalizeId(employeeId), LocalDate.now())
                .orElseThrow(() -> new ApiException(HttpStatus.CONFLICT, "Check in before recording a check-out."));
        if (attendance.getCheckOut() != null) {
            throw new ApiException(HttpStatus.CONFLICT, "Check-out has already been recorded today.");
        }
        if (attendance.getCheckIn() == null) {
            throw new ApiException(HttpStatus.CONFLICT, "Check in before recording a check-out.");
        }
        LocalTime checkOut = LocalTime.now().withSecond(0).withNano(0);
        long workedMinutes = Duration.between(attendance.getCheckIn(), checkOut).toMinutes();
        if (workedMinutes > 510 && workedMinutes < 540) {
            throw new ApiException(HttpStatus.CONFLICT, "Check out after 9 hours, or check out within 8.5 hours for half-day leave.");
        }
        attendance.setCheckOut(checkOut);
        attendance.setStatus(workedMinutes >= 540 ? "Present" : "Half day leave");
        return toResponse(attendanceRepository.save(attendance));
    }

    private Attendance newAttendance(String employeeId, LocalDate date) {
        Employee employee = employeeRepository.findByEmployeeIdIgnoreCase(normalizeId(employeeId))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Employee was not found."));
        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setWorkDate(date);
        attendance.setStatus("Present");
        return attendance;
    }

    private static String normalizeId(String id) { return id.trim().toUpperCase(Locale.ROOT); }

    private static AttendanceResponse toResponse(Attendance attendance) {
        Employee employee = attendance.getEmployee();
        String name = employee.getFirstName() + " " + employee.getLastName();
        return new AttendanceResponse(employee.getEmployeeId(), name, employee.getDesignation(), attendance.getWorkDate(),
                format(attendance.getCheckIn()), format(attendance.getCheckOut()), attendance.getStatus());
    }

    private static String format(LocalTime time) { return time == null ? "—" : time.format(TIME_FORMAT); }
}
