package com.slottify.appointment_scheduler.mapper;

import com.slottify.appointment_scheduler.dto.CreateAppointmentRequest;
import com.slottify.appointment_scheduler.dto.UpdateAppointmentRequest;
import com.slottify.appointment_scheduler.entity.Appointment;
import com.slottify.appointment_scheduler.entity.Item;
import com.slottify.appointment_scheduler.entity.Priority;
import com.slottify.appointment_scheduler.entity.UserProject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AppointmentMapper {

    @Mapping(source = "request.startDateTime", target = "startDateTime")
    @Mapping(source = "request.endDateTime", target = "endDateTime")
    @Mapping(source = "request.description", target = "description")
    @Mapping(source = "request.phoneNumber", target = "phoneNumber")
    @Mapping(source = "userProject", target = "userProject")
    @Mapping(source = "item", target = "item")
    @Mapping(source = "priority", target = "priority")
    Appointment toEntity(UserProject userProject, CreateAppointmentRequest request, Item item, Priority priority);

    Appointment updateAppointment(UpdateAppointmentRequest request, @MappingTarget Appointment entity);
}
