package com.slottify.appointment_scheduler.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAppointmentRequest {

    @NotNull(message = "sart date and time is mandatory")
    private LocalDateTime startDateTime;
    @NotNull(message = "end date time is mandatory")
    private LocalDateTime endDateTime;
    private String description;
    private String phoneNumber;
}
