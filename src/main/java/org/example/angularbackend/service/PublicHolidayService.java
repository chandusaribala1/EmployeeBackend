package org.example.angularbackend.service;

import java.util.List;
import java.util.Locale;
import org.example.angularbackend.dto.PublicHolidayRequest;
import org.example.angularbackend.dto.PublicHolidayResponse;
import org.example.angularbackend.entity.Employee;
import org.example.angularbackend.entity.PublicHoliday;
import org.example.angularbackend.repository.EmployeeRepository;
import org.example.angularbackend.repository.PublicHolidayRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PublicHolidayService {
    private final PublicHolidayRepository publicHolidayRepository;
    private final EmployeeRepository employeeRepository;
    public PublicHolidayService(PublicHolidayRepository publicHolidayRepository, EmployeeRepository employeeRepository) {
        this.publicHolidayRepository = publicHolidayRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<PublicHolidayResponse> findAll() {
        return publicHolidayRepository.findAllBySourceOrderByHolidayDateAsc("CUSTOM").stream()
                .map(PublicHolidayService::toResponse)
                .toList();
    }

    @Transactional
    public PublicHolidayResponse create(PublicHolidayRequest request) {
        requireHr(request.employeeId());
        if (publicHolidayRepository.findByHolidayDateAndName(request.date(), request.name().trim()).isPresent()) {
            throw new ApiException(HttpStatus.CONFLICT, "This holiday is already recorded for this date.");
        }
        PublicHoliday holiday = new PublicHoliday();
        holiday.setHolidayDate(request.date());
        holiday.setName(request.name().trim());
        holiday.setDescription(request.description() == null || request.description().isBlank() ? null : request.description().trim());
        holiday.setSource("CUSTOM");
        return toResponse(publicHolidayRepository.save(holiday));
    }

    @Transactional
    public void delete(Long holidayId, String employeeId) {
        requireHr(employeeId);
        if (!publicHolidayRepository.existsById(holidayId)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Public holiday was not found.");
        }
        PublicHoliday holiday = publicHolidayRepository.findById(holidayId).orElseThrow();
        publicHolidayRepository.delete(holiday);
    }

    private void requireHr(String employeeId) {
        Employee employee = employeeRepository.findByEmployeeIdIgnoreCase(employeeId.trim().toUpperCase(Locale.ROOT))
                .orElseThrow(() -> new ApiException(HttpStatus.FORBIDDEN, "Only an HR designation employee can manage public holidays."));
        String designation = employee.getDesignation().toLowerCase(Locale.ROOT);
        if (!designation.matches(".*\\bhr\\b.*") && !designation.contains("human resource")) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Only an HR designation employee can manage public holidays.");
        }
    }

    static PublicHolidayResponse toResponse(PublicHoliday holiday) {
        return new PublicHolidayResponse(holiday.getId(), holiday.getHolidayDate(), holiday.getName(), holiday.getDescription(), holiday.getSource());
    }
}
