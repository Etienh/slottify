package com.slottify.appointment_scheduler.repository;

import com.slottify.appointment_scheduler.entity.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProject, UUID> {

    @Query(value = "SELECT up FROM user_project AS up WHERE up.user_id = :userId AND up.project_id = :projectId", nativeQuery = true)
    Optional<UserProject> findByUserAndProject(@Param("userId") UUID userId, @Param("projectId") UUID projectId);
}
