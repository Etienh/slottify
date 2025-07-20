package com.slottify.appointment_scheduler.entity;

import com.slottify.appointment_scheduler.common.BaseEntity;
import com.slottify.appointment_scheduler.enums.ShiftType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "shift")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shift extends BaseEntity {

    @Column(nullable = false)
    private LocalTime startHour;
    @Column(nullable = false)
    private LocalTime endHour;
    @Column(length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private ShiftType type;
    @ManyToOne
    @JoinColumn(name = "user_project_id")
    private UserProject userProject;

}
