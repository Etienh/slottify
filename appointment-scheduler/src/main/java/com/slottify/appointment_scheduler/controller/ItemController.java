package com.slottify.appointment_scheduler.controller;

import com.slottify.appointment_scheduler.common.SessionUtils;
import com.slottify.appointment_scheduler.dto.CreateItemRequest;
import com.slottify.appointment_scheduler.dto.PageableResponse;
import com.slottify.appointment_scheduler.dto.UpdateItemRequest;
import com.slottify.appointment_scheduler.entity.Item;
import com.slottify.appointment_scheduler.service.ItemService;
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
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final SessionUtils sessionUtils;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreateItemRequest request) throws Exception {
        itemService.create(sessionUtils.getProjectIdInSession(), request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @Valid @RequestBody UpdateItemRequest request) throws Exception {
        itemService.update(id, request);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<PageableResponse<Item>> getAll(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) throws Exception {
        PageableResponse<Item> response = itemService.getAll(page, size, sessionUtils.getProjectIdInSession());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getById(@PathVariable UUID id) throws Exception {
        Item response = itemService.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}