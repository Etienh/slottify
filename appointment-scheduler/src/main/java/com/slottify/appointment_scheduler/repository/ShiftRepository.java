package com.slottify.appointment_scheduler.repository;

import com.slottify.appointment_scheduler.entity.Shift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, UUID> {

    @Query(value = "SELECT s FROM Shift s JOIN UserProject up ON s.userProject = up WHERE up.project.id = :projectId")
    Page<Shift> findAllShiftForProject(@Param("projectId") UUID projectId, Pageable pageable);

    @Query(value = "SELECT COUNT(s) > 0 FROM Shift s JOIN UserProject up ON s.userProject = up WHERE up.project.id = :projectId AND s.name = :shiftName")
    boolean isShiftNameAlreadyPresentForProject(@Param("projectId") UUID projectId, @Param("shiftName") String shiftName);
}
