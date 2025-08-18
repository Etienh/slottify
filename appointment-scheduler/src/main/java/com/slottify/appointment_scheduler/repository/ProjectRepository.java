package com.slottify.appointment_scheduler.repository;

import com.slottify.appointment_scheduler.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    @Query(value = "SELECT p FROM Project p JOIN UserProject up ON up.project = p WHERE up.id = :userId")
    Page<Project> findAllProjectForUser(@Param("userId") UUID userId, Pageable pageable);

    @Query(value = "SELECT COUNT(p) > 0 FROM Project p JOIN UserProject up ON up.project = p WHERE up.user.id = :userId AND p.name = :projectName")
    boolean isProjectNameAlreadyPresent(@Param("userId") UUID userId, @Param("projectName") String projectName);
}
