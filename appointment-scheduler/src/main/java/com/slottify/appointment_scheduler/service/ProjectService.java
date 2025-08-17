package com.slottify.appointment_scheduler.service;

import com.slottify.appointment_scheduler.common.SessionUtils;
import com.slottify.appointment_scheduler.dto.CreateProjectRequest;
import com.slottify.appointment_scheduler.dto.PageableResponse;
import com.slottify.appointment_scheduler.dto.UpdateProjectRequest;
import com.slottify.appointment_scheduler.entity.Project;
import com.slottify.appointment_scheduler.mapper.ProjectMapper;
import com.slottify.appointment_scheduler.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final SessionUtils sessionUtils;

    public Project create(CreateProjectRequest request) {
        Project entityToSave = projectMapper.toEntity(request);
        return projectRepository.save(entityToSave);
    }

    public Project update(UUID projectId, UpdateProjectRequest request) {
        Project project = projectRepository.findById(projectId).orElseThrow(EntityNotFoundException::new);
        Project updatedProject = projectMapper.updateProject(request, project);
        return projectRepository.save(updatedProject);
    }

    public PageableResponse<Project> getAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Project> projectPage = projectRepository.findAllProjectForUser(sessionUtils.getUserInSession().getId(), pageRequest);
        return PageableResponse.<Project>builder().
                elements(projectPage.getContent())
                .page(projectPage.getNumber() + 1)
                .size(projectPage.getSize())
                .totalPages(projectPage.getTotalPages())
                .totalElements(projectPage.getTotalElements())
                .build();
    }

    public Project getById(UUID projectId) {
        return projectRepository.findById(projectId).orElseThrow(EntityNotFoundException::new);
    }


}
