package com.slottify.appointment_scheduler.mapper;


import com.slottify.appointment_scheduler.dto.CreatePriorityRequest;
import com.slottify.appointment_scheduler.dto.UpdatePriorityRequest;
import com.slottify.appointment_scheduler.entity.Priority;
import com.slottify.appointment_scheduler.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PriorityMapper {

    @Mapping(source = "request.name", target = "name")
    @Mapping(source = "request.level", target = "level")
    @Mapping(source = "request.color", target = "color")
    @Mapping(source = "project", target = "project")
    Priority toEntity(Project project, CreatePriorityRequest request);

    Priority updatePriority(UpdatePriorityRequest request, @MappingTarget Priority entity);
}

