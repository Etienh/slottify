package com.slottify.appointment_scheduler.repository;

import com.slottify.appointment_scheduler.entity.Priority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, UUID> {

    @Query(value = "SELECT COUNT(p) > 0 FROM Priority p JOIN Project AS pr on p.project = pr WHERE p.name = :priorityName")
    boolean isPriorityNameAlreadyPresent(UUID projectId, String priorityName);

    @Query(value = "SELECT p from Priority p JOIN Project pr ON p.project = pr WHERE pr.id = :projectId")
    Page<Priority> findAllPriorityForProject(@Param("projectId") UUID projectId, Pageable pageable);
}
