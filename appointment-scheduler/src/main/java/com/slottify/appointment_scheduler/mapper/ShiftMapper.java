package com.slottify.appointment_scheduler.mapper;

import com.slottify.appointment_scheduler.dto.CreateShiftRequest;
import com.slottify.appointment_scheduler.dto.UpdateShiftRequest;
import com.slottify.appointment_scheduler.entity.Shift;
import com.slottify.appointment_scheduler.entity.UserProject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ShiftMapper {

    @Mapping(source = "request.name", target = "name")
    @Mapping(source = "request.startHour", target = "startHour")
    @Mapping(source = "request.endHour", target = "endHour")
    @Mapping(source = "request.type", target = "type")
    @Mapping(source = "userProject", target = "userProject")
    Shift toEntity(UserProject userProject, CreateShiftRequest request);

    Shift updateShift(UpdateShiftRequest request, @MappingTarget Shift entity);
}
