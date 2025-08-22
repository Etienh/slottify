package com.slottify.appointment_scheduler.service;

import com.slottify.appointment_scheduler.dto.CreatePriorityRequest;
import com.slottify.appointment_scheduler.dto.PageableResponse;
import com.slottify.appointment_scheduler.dto.UpdatePriorityRequest;
import com.slottify.appointment_scheduler.entity.Priority;
import com.slottify.appointment_scheduler.entity.Project;
import com.slottify.appointment_scheduler.exceptions.BadRequestException;
import com.slottify.appointment_scheduler.mapper.PriorityMapper;
import com.slottify.appointment_scheduler.repository.PriorityRepository;
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
public class PriorityService {

    private final PriorityRepository priorityRepository;
    private final PriorityMapper priorityMapper;
    private final ProjectService projectService;


    public void create(UUID projectId, CreatePriorityRequest request) throws Exception{
        Project project = projectService.getById(projectId);
        if(priorityRepository.isPriorityNameAlreadyPresent(projectId, request.getName())){
            throw new BadRequestException("priority with name: " + request.getName() + " already exists for project: " + project.getName());
        }
        Priority entityToSave = priorityMapper.toEntity(project, request);
        priorityRepository.save(entityToSave);
    }

    public void update(UUID priorityId, UpdatePriorityRequest request) throws Exception{
        Priority priority = priorityRepository.findById(priorityId).orElseThrow(() -> new EntityNotFoundException("priority not found"));
        Priority updatedPriority = priorityMapper.updatePriority(request, priority);
        priorityRepository.save(updatedPriority);
    }

    public PageableResponse<Priority> getAll(int page, int size, UUID projectId){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Priority> priorityPage = priorityRepository.findAllPriorityForProject(projectId, pageRequest);
        return PageableResponse.<Priority>builder()
                .elements(priorityPage.getContent())
                .page(priorityPage.getNumber())
                .size(priorityPage.getSize())
                .totalPages(priorityPage.getTotalPages())
                .totalElements(priorityPage.getTotalElements())
                .build();
    }

    public Priority getById(UUID priorityId){
        return priorityRepository.findById(priorityId).orElseThrow(() -> new EntityNotFoundException("priority not found"));
    }


}
