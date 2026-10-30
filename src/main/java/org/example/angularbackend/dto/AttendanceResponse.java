package org.example.angularbackend.dto;

import java.time.LocalDate;

public record AttendanceResponse(String employeeId, String name, String designation, LocalDate workDate,
                                 String checkIn, String checkOut, String status) { }
