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
import lombok.NoArgsConstructor;

@Entity
@Table(name = "priority")
@Data
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

}
