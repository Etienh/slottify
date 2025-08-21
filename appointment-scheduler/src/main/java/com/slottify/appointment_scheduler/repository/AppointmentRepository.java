package com.slottify.appointment_scheduler.repository;


import com.slottify.appointment_scheduler.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    @Query(value = "SELECT a FROM Appointment a Join UserProject up on a.userProject = up WHERE up.project.id = :projectId")
    Page<Appointment> findAllAppointmentForProject(@Param("projectId") UUID projectId, Pageable pageable);

    @Query(value = "SELECT a FROM Appointment a Join UserProject up on a.userProject = up WHERE up.id = :userProjectId")
    Page<Appointment> findAllAppointmentForUserProject(@Param("userProjectId") UUID userProjectId, Pageable pageable);

    @Query("SELECT a  FROM Appointment a JOIN UserProject up ON a.userProject = up WHERE up.project.id = :projectId AND a.startDateTime < :end AND a.endDateTime  > :start")
    List<Appointment> findOverlappingAppointmentsForProject(UUID projectId, LocalDateTime start, LocalDateTime end);

}
