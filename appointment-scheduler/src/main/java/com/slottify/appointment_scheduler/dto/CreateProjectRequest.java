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

    @NotBlank(message = "name is mandatory")
    private String name;
    private String description;
    private boolean status;
    private boolean userSharedCalendar;
    private boolean itemSharedCalendar;
    @Max(value = 1440, message = "time slot can't be greater than 1440 min (24 hours)")
    @Min(value = 5, message = "time slot can't be lower than 5 min")
    private Integer timeSlot;
}
