package com.slottify.appointment_scheduler.repository;

import com.slottify.appointment_scheduler.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    @Query(value = "SELECT p FROM project AS p JOIN user_project AS up ON up.user_id = :userId", nativeQuery = true)
    Page<Project> findAllProjectForUser(@Param("userId") UUID userId, Pageable pageable);

    @Query(value = "SELECT COUNT(p) FROM project AS p JOIN user_project AS up ON up.user_id = :userId WHERE p.name = :projectName", nativeQuery = true)
    int isProjectNameAlreadyPresent(@Param("userId") UUID userId, @Param("projectName") String projectName);
}
