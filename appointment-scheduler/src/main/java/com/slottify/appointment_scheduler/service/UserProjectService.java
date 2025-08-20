package com.slottify.appointment_scheduler.service;

import com.slottify.appointment_scheduler.common.SessionUtils;
import com.slottify.appointment_scheduler.dto.CreateProjectRequest;
import com.slottify.appointment_scheduler.entity.Project;
import com.slottify.appointment_scheduler.entity.User;
import com.slottify.appointment_scheduler.entity.UserProject;
import com.slottify.appointment_scheduler.enums.AccessType;
import com.slottify.appointment_scheduler.mapper.UserProjectMapper;
import com.slottify.appointment_scheduler.repository.UserProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProjectService {

    private final UserProjectRepository userProjectRepository;
    private final UserProjectMapper userProjectMapper;
    private final ProjectService projectService;
    private final SessionUtils sessionUtils;


    @Transactional
    public UserProject create(CreateProjectRequest request) throws Exception{
        User user = sessionUtils.getUserInSession();
        Project project = projectService.create(request);
        UserProject entityToSave = userProjectMapper.toEntity(user, project, AccessType.ADMIN);
        return userProjectRepository.save(entityToSave);
    }

    public UserProject getByProjectForUserInSession(UUID projectId) throws Exception{
       return userProjectRepository.findByUserAndProject(sessionUtils.getUserInSession().getId(), projectId).orElseThrow(() -> new EntityNotFoundException("project for user not found"));
    }

}
