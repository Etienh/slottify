package com.slottify.appointment_scheduler.dto;

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
    private Integer timeSlot;

}
