package org.example.angularbackend.dto;

import java.time.LocalDate;

public record PublicHolidayResponse(Long id, LocalDate date, String name, String description, String source) { }
