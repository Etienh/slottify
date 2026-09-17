package com.slottify.appointment_scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateItemRequest {

    private String name;
    private String description;
    private Map<String, String> attributes;
    private Integer timeSlot;
}
