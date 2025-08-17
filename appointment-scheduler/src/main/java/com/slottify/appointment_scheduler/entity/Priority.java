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

import java.util.Objects;

@Entity
@Table(name = "priority")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Priority extends BaseEntity {

    @Column(name = "name")
    private String name;
    @Column(nullable = false)
    private int level;
    @Column(name = "color")
    private String color;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Priority priority)) return false;
        if (!super.equals(o)) return false;
        return getLevel() == priority.getLevel() && Objects.equals(getName(), priority.getName()) && Objects.equals(getColor(), priority.getColor()) && Objects.equals(getProject(), priority.getProject());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName(), getLevel(), getColor(), getProject());
    }

    @Override
    public String toString() {
        return super.toString() + '\n' +
                "Priority{" +
                "name='" + name + '\'' +
                ", level=" + level +
                ", color='" + color + '\'' +
                ", project=" + project +
                '}';
    }
}
