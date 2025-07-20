package com.slottify.appointment_scheduler.entity;

import com.slottify.appointment_scheduler.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Table(name = "project")
@Data
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

}
