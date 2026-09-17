package com.slottify.appointment_scheduler.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePriorityRequest {

    @NotBlank(message = "name is mandatory")
    private String name;
    @Max(value = 100, message = "level can't be greater than 100")
    @Min(value = 1, message = "level can't be lower than 1")
    private int level;
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Invalid hex color")
    private String color;
}
