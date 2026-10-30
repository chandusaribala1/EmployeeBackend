package org.example.angularbackend.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.example.angularbackend.dto.LeaveRequestCreate;
import org.example.angularbackend.dto.LeaveRequestResponse;
import org.example.angularbackend.dto.LeaveStatusUpdate;
import org.example.angularbackend.service.LeaveRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) { this.leaveRequestService = leaveRequestService; }

    @GetMapping
    public List<LeaveRequestResponse> findAll() { return leaveRequestService.findAll(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LeaveRequestResponse create(@Valid @RequestBody LeaveRequestCreate request) { return leaveRequestService.create(request); }

    @PutMapping("/{id}")
    public LeaveRequestResponse update(@PathVariable String id, @Valid @RequestBody LeaveStatusUpdate request) {
        return leaveRequestService.update(id, request);
    }
}
