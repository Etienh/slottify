package com.slottify.appointment_scheduler.service;

import com.slottify.appointment_scheduler.entity.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final ProjectService projectService;


    @Cacheable(cacheNames = "projectInSessionCache", key = "#username", condition = "#username != null")
    public Project getUserProjectInSessionByUsername(String username, UUID projectId) throws Exception {
        return projectService.getById(projectId);
    }

    @CachePut(cacheNames = "projectInSessionCache", key = "#username", condition = "#username != null")
    public Project updateUserProjectCache(String username, UUID projectId) throws Exception {
        return projectService.getById(projectId);
    }

}
