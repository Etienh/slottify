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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "shift")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shift extends BaseEntity {

    @Column(name = "name")
    private String name;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Shift shift)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getName(), shift.getName()) && Objects.equals(getStartHour(), shift.getStartHour()) && Objects.equals(getEndHour(), shift.getEndHour()) && getType() == shift.getType() && Objects.equals(getUserProject(), shift.getUserProject());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName(), getStartHour(), getEndHour(), getType(), getUserProject());
    }

    @Override
    public String toString() {
        return super.toString() + '\n' +
                "Shift{" +
                "name='" + name + '\'' +
                ", startHour=" + startHour +
                ", endHour=" + endHour +
                ", type=" + type +
                ", userProject=" + userProject +
                '}';
    }
}
