package com.slottify.appointment_scheduler.controller;


import com.slottify.appointment_scheduler.dto.CreateProjectRequest;
import com.slottify.appointment_scheduler.service.UserProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/project")
@RequiredArgsConstructor
public class UserProjectController {

    private final UserProjectService userProjectService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateProjectRequest request){
        userProjectService.create(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
