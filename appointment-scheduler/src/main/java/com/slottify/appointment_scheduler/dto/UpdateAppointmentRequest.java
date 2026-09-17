package com.slottify.appointment_scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAppointmentRequest {

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String description;
    private String phoneNumber;

}
