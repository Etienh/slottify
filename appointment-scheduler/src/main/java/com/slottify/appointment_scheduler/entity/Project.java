package com.slottify.appointment_scheduler.entity;

import com.slottify.appointment_scheduler.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "project")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private boolean status;
    @Column(name = "user_shared_calendar")
    private boolean userSharedCalendar;
    @Column(name = "item_shared_calendar")
    private boolean itemSharedCalendar;
    @Column(name = "time_slot")
    private Integer timeSlot;
    @OneToMany(mappedBy = "project")
    private List<Item> items;
    @OneToMany(mappedBy = "project")
    private List<Priority> priorities;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Project project)) return false;
        if (!super.equals(o)) return false;
        return isStatus() == project.isStatus() && isUserSharedCalendar() == project.isUserSharedCalendar() && isItemSharedCalendar() == project.isItemSharedCalendar() && Objects.equals(getName(), project.getName()) && Objects.equals(getDescription(), project.getDescription()) && Objects.equals(getTimeSlot(), project.getTimeSlot()) && Objects.equals(getItems(), project.getItems()) && Objects.equals(getPriorities(), project.getPriorities());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName(), getDescription(), isStatus(), isUserSharedCalendar(), isItemSharedCalendar(), getTimeSlot(), getItems(), getPriorities());
    }

    @Override
    public String toString() {
        return super.toString() + '\n' +
                "Project{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", userSharedCalendar=" + userSharedCalendar +
                ", itemSharedCalendar=" + itemSharedCalendar +
                ", timeSlot=" + timeSlot +
                ", items=" + items +
                ", priorities=" + priorities +
                '}';
    }
}
