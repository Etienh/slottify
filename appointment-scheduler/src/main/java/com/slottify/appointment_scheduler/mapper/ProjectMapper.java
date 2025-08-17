package com.slottify.appointment_scheduler.mapper;

import com.slottify.appointment_scheduler.dto.CreateProjectRequest;
import com.slottify.appointment_scheduler.dto.UpdateProjectRequest;
import com.slottify.appointment_scheduler.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProjectMapper {

    Project toEntity(CreateProjectRequest request);
    Project updateProject(UpdateProjectRequest request, @MappingTarget Project entity);
}
