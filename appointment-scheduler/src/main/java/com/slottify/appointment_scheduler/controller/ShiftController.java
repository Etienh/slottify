package com.slottify.appointment_scheduler.controller;

import com.slottify.appointment_scheduler.common.SessionUtils;
import com.slottify.appointment_scheduler.dto.CreateShiftRequest;
import com.slottify.appointment_scheduler.dto.PageableResponse;
import com.slottify.appointment_scheduler.dto.UpdateShiftRequest;
import com.slottify.appointment_scheduler.entity.Shift;
import com.slottify.appointment_scheduler.service.ShiftService;
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
@RequestMapping("/shift")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;
    private final SessionUtils sessionUtils;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateShiftRequest request) throws Exception {
        shiftService.create(sessionUtils.getProjectIdInSession(), request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody UpdateShiftRequest request) throws Exception {
        shiftService.update(id, request);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<Shift>> getAll(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        PageableResponse<Shift> response = shiftService.getAll(page, size, sessionUtils.getProjectIdInSession());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shift> getById(@PathVariable UUID id) throws Exception {
        Shift response = shiftService.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}