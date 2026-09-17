package com.slottify.appointment_scheduler.mapper;

import com.slottify.appointment_scheduler.entity.Project;
import com.slottify.appointment_scheduler.entity.User;
import com.slottify.appointment_scheduler.entity.UserProject;
import com.slottify.appointment_scheduler.enums.AccessType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserProjectMapper {

    @Mapping(source = "user", target = "user")
    @Mapping(source = "project", target = "project")
    @Mapping(source = "accessType", target = "accessType")
    @Mapping(target = "id", ignore = true)
    UserProject toEntity(User user, Project project, AccessType accessType);
}
