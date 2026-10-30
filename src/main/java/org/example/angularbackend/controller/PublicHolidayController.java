package org.example.angularbackend.controller;

import java.util.List;
import jakarta.validation.Valid;
import org.example.angularbackend.dto.PublicHolidayRequest;
import org.example.angularbackend.dto.PublicHolidayResponse;
import org.example.angularbackend.service.PublicHolidayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public-holidays")
public class PublicHolidayController {
    private final PublicHolidayService publicHolidayService;

    public PublicHolidayController(PublicHolidayService publicHolidayService) {
        this.publicHolidayService = publicHolidayService;
    }

    @GetMapping
    public List<PublicHolidayResponse> findAll() { return publicHolidayService.findAll(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublicHolidayResponse create(@Valid @RequestBody PublicHolidayRequest request) { return publicHolidayService.create(request); }

    @DeleteMapping("/{holidayId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long holidayId, @RequestParam String employeeId) {
        publicHolidayService.delete(holidayId, employeeId);
    }
}
