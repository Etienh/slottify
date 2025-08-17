package com.slottify.appointment_scheduler.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectRequest {

    @NotBlank
    private String name;
    private String description;
    private boolean status;
    private boolean userSharedCalendar;
    private boolean itemSharedCalendar;
    @Max(1440)
    @Min(5)
    private Integer timeSlot;
}
