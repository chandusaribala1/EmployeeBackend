package org.example.angularbackend.controller;

import java.util.List;
import org.example.angularbackend.dto.AttendanceResponse;
import org.example.angularbackend.service.AttendanceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) { this.attendanceService = attendanceService; }

    @GetMapping
    public List<AttendanceResponse> findToday(@RequestParam String employeeId) { return attendanceService.findToday(employeeId); }

    @GetMapping("/month")
    public List<AttendanceResponse> findMonth(@RequestParam String employeeId, @RequestParam int year,
                                               @RequestParam int month) {
        return attendanceService.findMonth(employeeId, year, month);
    }

    @PutMapping("/{employeeId}/check-in")
    public AttendanceResponse checkIn(@PathVariable String employeeId) { return attendanceService.checkIn(employeeId); }

    @PutMapping("/{employeeId}/check-out")
    public AttendanceResponse checkOut(@PathVariable String employeeId) { return attendanceService.checkOut(employeeId); }
}
