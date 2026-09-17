package com.slottify.appointment_scheduler.dto;

import com.slottify.appointment_scheduler.enums.ShiftType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateShiftRequest {

    @NotBlank
    private String name;
    @NotNull
    private LocalTime startHour;
    @NotNull
    private LocalTime endHour;
    @NotNull
    @Pattern(regexp = "^(WORK|BREAK)$", message = "Type must be WORK or BREAK")
    private ShiftType type;

}
