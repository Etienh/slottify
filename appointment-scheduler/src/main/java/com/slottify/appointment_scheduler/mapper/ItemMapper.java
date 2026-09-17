package com.slottify.appointment_scheduler.mapper;

import com.slottify.appointment_scheduler.dto.CreateItemRequest;
import com.slottify.appointment_scheduler.dto.UpdateItemRequest;
import com.slottify.appointment_scheduler.entity.Item;
import com.slottify.appointment_scheduler.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemMapper {

    @Mapping(source = "request.name", target = "name")
    @Mapping(source = "request.description", target = "description")
    @Mapping(source = "request.attributes", target = "attributes")
    @Mapping(source = "request.timeSlot", target = "timeSlot")
    @Mapping(source = "project", target = "project")
    Item toEntity(Project project, CreateItemRequest request);

    Item updateItem(UpdateItemRequest request, @MappingTarget Item entity);
}
