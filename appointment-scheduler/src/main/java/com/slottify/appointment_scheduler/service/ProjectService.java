package com.slottify.appointment_scheduler.service;

import com.slottify.appointment_scheduler.common.SessionUtils;
import com.slottify.appointment_scheduler.dto.CreateProjectRequest;
import com.slottify.appointment_scheduler.dto.PageableResponse;
import com.slottify.appointment_scheduler.dto.UpdateProjectRequest;
import com.slottify.appointment_scheduler.entity.Project;
import com.slottify.appointment_scheduler.exceptions.BadRequestException;
import com.slottify.appointment_scheduler.mapper.ProjectMapper;
import com.slottify.appointment_scheduler.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final SessionUtils sessionUtils;

    public Project create(CreateProjectRequest request) throws Exception{
        Project entityToSave = projectMapper.toEntity(request);
        if(isProjectNameAlreadyPresent(entityToSave.getName())){
            throw new BadRequestException("project with name: " +  entityToSave.getName() +  " already exists");
        }
        return projectRepository.save(entityToSave);
    }

    public Project update(UUID projectId, UpdateProjectRequest request) throws Exception{
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("project not found"));
        if(request.getName() != null && isProjectNameAlreadyPresent(request.getName())){
            throw new BadRequestException("project with name: " +  request.getName() +  " already exists");
        }
        Project updatedProject = projectMapper.updateProject(request, project);
        return projectRepository.save(updatedProject);
    }

    public PageableResponse<Project> getAll(int page, int size) throws Exception{
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Project> projectPage = projectRepository.findAllProjectForUser(sessionUtils.getUserInSession().getId(), pageRequest);
        return PageableResponse.<Project>builder().
                elements(projectPage.getContent())
                .page(projectPage.getNumber())
                .size(projectPage.getSize())
                .totalPages(projectPage.getTotalPages())
                .totalElements(projectPage.getTotalElements())
                .build();
    }

    public Project getById(UUID projectId) throws Exception {
        return projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("project not found"));
    }


    private boolean isProjectNameAlreadyPresent(String projectName){
        UUID userId = sessionUtils.getUserInSession().getId();
        return projectRepository.isProjectNameAlreadyPresent(userId, projectName);
    }


}
