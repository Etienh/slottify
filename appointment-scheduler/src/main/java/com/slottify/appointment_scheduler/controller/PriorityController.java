package com.slottify.appointment_scheduler.controller;

import com.slottify.appointment_scheduler.dto.CreatePriorityRequest;
import com.slottify.appointment_scheduler.dto.PageableResponse;
import com.slottify.appointment_scheduler.dto.UpdatePriorityRequest;
import com.slottify.appointment_scheduler.entity.Priority;
import com.slottify.appointment_scheduler.service.PriorityService;
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
@RequestMapping("/priority")
@RequiredArgsConstructor
public class PriorityController {

    private final PriorityService priorityService;


    @PostMapping
    public ResponseEntity<Void> create(@RequestParam UUID projectId, @Valid @RequestBody CreatePriorityRequest request) throws Exception{
        priorityService.create(projectId, request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestParam(required = false) UUID projectId, @Valid @RequestBody UpdatePriorityRequest request) throws Exception{
        priorityService.update(id, projectId, request);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<Priority>> getAll(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam UUID projectId) throws Exception{
        PageableResponse<Priority> response = priorityService.getAll(page, size, projectId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Priority> getById(@PathVariable UUID id) throws Exception{
        Priority response = priorityService.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
