package org.example.angularbackend.dto;

import java.time.LocalDate;

public record LeaveRequestResponse(String id, String employeeId, String employeeName, String type,
                                  LocalDate startDate, LocalDate endDate, long days, String status, String reason) { }
