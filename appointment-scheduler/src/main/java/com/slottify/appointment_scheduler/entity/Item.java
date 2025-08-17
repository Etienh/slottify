package com.slottify.appointment_scheduler.entity;


import com.slottify.appointment_scheduler.common.BaseEntity;
import com.slottify.appointment_scheduler.common.JsonMapConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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

import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description")
    private String description;
    @Convert(converter = JsonMapConverter.class)
    @Column(name = "attributes", columnDefinition = "json")
    private Map<String, String> attributes;
    @Column(name = "time_slot")
    private Integer timeSlot;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Item item)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getName(), item.getName()) && Objects.equals(getDescription(), item.getDescription()) && Objects.equals(getAttributes(), item.getAttributes()) && Objects.equals(getTimeSlot(), item.getTimeSlot()) && Objects.equals(getProject(), item.getProject());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName(), getDescription(), getAttributes(), getTimeSlot(), getProject());
    }

    @Override
    public String toString() {
        return super.toString() + '\n' +
                "Item{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", attributes=" + attributes +
                ", timeSlot=" + timeSlot +
                ", project=" + project +
                '}';
    }
}
