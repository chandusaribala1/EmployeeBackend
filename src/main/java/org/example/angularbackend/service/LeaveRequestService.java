package org.example.angularbackend.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.example.angularbackend.entity.Employee;
import org.example.angularbackend.dto.LeaveRequestCreate;
import org.example.angularbackend.dto.LeaveRequestResponse;
import org.example.angularbackend.dto.LeaveStatusUpdate;
import org.example.angularbackend.entity.Employee;
import org.example.angularbackend.entity.LeaveRequest;
import org.example.angularbackend.repository.EmployeeRepository;
import org.example.angularbackend.repository.PublicHolidayRepository;
import org.example.angularbackend.repository.LeaveRequestRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LeaveRequestService {
    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final PublicHolidayRepository publicHolidayRepository;
    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository, EmployeeRepository employeeRepository,
                               PublicHolidayRepository publicHolidayRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
        this.publicHolidayRepository = publicHolidayRepository;
    }

    @Transactional(readOnly = true)
    public List<LeaveRequestResponse> findAll() {
        return leaveRequestRepository.findAllByOrderByStartDateDesc().stream().map(this::toResponse).toList();
    }

    @Transactional
    public LeaveRequestResponse create(LeaveRequestCreate request) {
        if (request.endDate().isBefore(request.startDate())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "End date must be on or after start date.");
        }
        long workingDays = workingDays(request.startDate(), request.endDate());
        if (workingDays == 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "The selected date range contains no working days.");
        }
        Employee employee = employeeRepository.findByEmployeeIdIgnoreCase(request.employeeId().trim())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Employee was not found."));
        LeaveRequest entity = new LeaveRequest();
        entity.setEmployee(employee);
        entity.setType(request.type().trim());
        entity.setStartDate(request.startDate());
        entity.setEndDate(request.endDate());
        entity.setReason(request.reason().trim());
        entity.setStatus("Pending");
        entity.setWorkingDays(Math.toIntExact(workingDays));
        return toResponse(leaveRequestRepository.save(entity));
    }

    @Transactional
    public LeaveRequestResponse update(String id, LeaveStatusUpdate update) {
        Employee reviewer = employeeRepository.findByEmployeeIdIgnoreCase(update.reviewerEmployeeId().trim())
                .orElseThrow(() -> new ApiException(HttpStatus.FORBIDDEN, "An HR employee must approve leave requests."));
        if (!isHr(reviewer.getDesignation())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Only an HR designation employee can approve or decline leave requests.");
        }
        long requestId;
        try { requestId = Long.parseLong(id); }
        catch (NumberFormatException exception) { throw new ApiException(HttpStatus.NOT_FOUND, "Leave request was not found."); }
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Leave request was not found."));
        request.setStatus(update.status());
        return toResponse(leaveRequestRepository.save(request));
    }

    private LeaveRequestResponse toResponse(LeaveRequest request) {
        Employee employee = request.getEmployee();
        return new LeaveRequestResponse(String.valueOf(request.getId()), employee.getEmployeeId(),
                employee.getFirstName() + " " + employee.getLastName(), request.getType(), request.getStartDate(),
                request.getEndDate(), request.getWorkingDays(),
                request.getStatus(), request.getReason());
    }

    private long workingDays(LocalDate startDate, LocalDate endDate) {
        Set<LocalDate> holidays = new HashSet<>(publicHolidayRepository
                .findAllByHolidayDateBetweenAndSourceOrderByHolidayDateAsc(startDate, endDate, "CUSTOM").stream()
                .map(holiday -> holiday.getHolidayDate()).toList());
        long count = 0;
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            DayOfWeek day = date.getDayOfWeek();
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY && !holidays.contains(date)) count++;
        }
        return count;
    }

    private static boolean isHr(String designation) {
        String normalized = designation.toLowerCase(Locale.ROOT);
        return normalized.matches(".*\\bhr\\b.*") || normalized.contains("human resource");
    }
}
