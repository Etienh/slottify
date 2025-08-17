package com.slottify.appointment_scheduler.entity;


import com.slottify.appointment_scheduler.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment extends BaseEntity {

    @Column(nullable = false)
    private LocalDateTime startDateTime;
    @Column(nullable = false)
    private LocalDateTime endDateTime;
    @Column(name = "description")
    private String description;
    @Column(name = "phone_number")
    private String phoneNumber;
    @ManyToOne
    @JoinColumn(name = "user_project_id")
    private UserProject userProject;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "priority_id")
    private Priority priority;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Appointment that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getStartDateTime(), that.getStartDateTime()) && Objects.equals(getEndDateTime(), that.getEndDateTime()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getPhoneNumber(), that.getPhoneNumber()) && Objects.equals(getUserProject(), that.getUserProject()) && Objects.equals(getItem(), that.getItem()) && Objects.equals(getPriority(), that.getPriority());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getStartDateTime(), getEndDateTime(), getDescription(), getPhoneNumber(), getUserProject(), getItem(), getPriority());
    }

    @Override
    public String toString() {
        return super.toString() + '\n' +
                "Appointment{" +
                "startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", description='" + description + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", userProject=" + userProject +
                ", item=" + item +
                ", priority=" + priority +
                '}';
    }
}
