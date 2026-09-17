package com.slottify.appointment_scheduler.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProjectRequest {


    private String name;
    private String description;
    private Boolean status;
    private Boolean userSharedCalendar;
    private Boolean itemSharedCalendar;
    @Max(value = 1440, message = "time slot can't be greater than 1440 min (24 hours)")
    @Min(value = 5, message = "time slot can't be lower than 5 min")
    private Integer timeSlot;

}
