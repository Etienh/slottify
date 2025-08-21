package com.slottify.appointment_scheduler.controller;


import com.slottify.appointment_scheduler.dto.CreateAppointmentRequest;
import com.slottify.appointment_scheduler.dto.PageableResponse;
import com.slottify.appointment_scheduler.dto.UpdateAppointmentRequest;
import com.slottify.appointment_scheduler.entity.Appointment;
import com.slottify.appointment_scheduler.service.AppointmentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestParam(required = false) UUID userId,
                                       @RequestParam UUID projectId,
                                       @RequestParam(required = false) UUID itemId,
                                       @RequestParam(required = false) UUID priorityId,
                                       @Valid @RequestBody CreateAppointmentRequest request) throws Exception {
        appointmentService.create(userId, projectId, itemId, priorityId, request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") UUID appointmentId,
                                       @RequestParam(required = false) UUID userId,
                                       @RequestParam UUID projectId,
                                       @RequestParam(required = false) UUID itemId,
                                       @RequestParam(required = false) UUID priorityId,
                                       @Valid @RequestBody UpdateAppointmentRequest request) throws Exception {
        appointmentService.update(userId, projectId, itemId, priorityId, appointmentId, request);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{id}/unassign-item")
    public ResponseEntity<Void> unassignItem(@PathVariable("id") UUID appointmentId) throws Exception {
        appointmentService.UnassignItem(appointmentId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{id}/unassign-priority")
    public ResponseEntity<Void> unassignPriority(@PathVariable("id") UUID appointmentId) throws Exception {
        appointmentService.UnassignPriority(appointmentId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<Appointment>> getAllFiltered(@RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size,
                                                                        @RequestParam UUID projectId,
                                                                        @RequestParam(required = false) UUID userId) throws Exception {
        var response = appointmentService.getAllFiltered(page, size, projectId, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getById(@PathVariable("id") UUID appointmentId) throws Exception {
        var response = appointmentService.getById(appointmentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
