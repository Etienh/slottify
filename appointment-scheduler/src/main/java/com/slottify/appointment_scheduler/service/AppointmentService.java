package com.slottify.appointment_scheduler.service;

import com.slottify.appointment_scheduler.dto.CreateAppointmentRequest;
import com.slottify.appointment_scheduler.dto.PageableResponse;
import com.slottify.appointment_scheduler.dto.UpdateAppointmentRequest;
import com.slottify.appointment_scheduler.entity.Appointment;
import com.slottify.appointment_scheduler.entity.Item;
import com.slottify.appointment_scheduler.entity.Priority;
import com.slottify.appointment_scheduler.entity.Project;
import com.slottify.appointment_scheduler.entity.UserProject;
import com.slottify.appointment_scheduler.exceptions.BadRequestException;
import com.slottify.appointment_scheduler.mapper.AppointmentMapper;
import com.slottify.appointment_scheduler.repository.AppointmentRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final ItemService itemService;
    private final PriorityService priorityService;
    private final UserProjectService userProjectService;
    private final ProjectService projectService;


    @Transactional
    public void create(UUID userId, UUID projectId, UUID itemId, UUID priorityId, CreateAppointmentRequest request) throws Exception{
        UserProject userProject = userId != null ? userProjectService.getByProjectAndUser(projectId, userId) : userProjectService.getByProjectForUserInSession(projectId);
        Item item  = itemId != null ? itemService.getById(itemId) : null;
        Priority priority = priorityId != null ? priorityService.getById(priorityId) : null;

        if(isAppointmentOverlapping(userProject.getUser().getId(), projectId, itemId, request.getStartDateTime(), request.getEndDateTime(), null)){
            throw new BadRequestException("an appointment for user or item already exists during this date and time");
        }
        Appointment entityToSave = appointmentMapper.toEntity(userProject, request, item, priority);
        appointmentRepository.save(entityToSave);

    }

    @Transactional
    public void update(UUID userId, UUID projectId, UUID itemId, UUID priorityId, UUID appointmentId,  UpdateAppointmentRequest request) throws Exception{
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new EntityNotFoundException("appointment not found"));
        Appointment updatedAppointment = appointmentMapper.updateAppointment(request, appointment);
        if(userId != null){
            updatedAppointment.setUserProject(userProjectService.getByProjectAndUser(projectId, userId));
        }
        if(itemId != null){
            updatedAppointment.setItem(itemService.getById(itemId));
        }
        if(priorityId != null){
            updatedAppointment.setPriority(priorityService.getById(priorityId));
        }
        if(request.getStartDateTime() != null && request.getEndDateTime() != null &&
            isAppointmentOverlapping(updatedAppointment.getUserProject().getUser().getId(), projectId, updatedAppointment.getItem() == null ? null : updatedAppointment.getItem().getId(),
                    updatedAppointment.getStartDateTime(), updatedAppointment.getEndDateTime(), updatedAppointment.getId())){
            throw new BadRequestException("an appointment for user or item already exists during this date and time");
        }
        appointmentRepository.save(updatedAppointment);
    }

    public void unassignItem(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new EntityNotFoundException("appointment not found"));
        appointment.setItem(null);
        appointmentRepository.save(appointment);
    }

    public void unassignPriority(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(() -> new EntityNotFoundException("appointment not found"));
        appointment.setPriority(null);
        appointmentRepository.save(appointment);
    }

    public PageableResponse<Appointment> getAllFiltered(int page, int size, UUID projectId, UUID userId) throws Exception{
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Appointment> appointmentPage;
        if(userId != null){
            UserProject userProject = userProjectService.getByProjectAndUser(projectId, userId);
            appointmentPage = appointmentRepository.findAllAppointmentForUserProject(userProject.getId(), pageRequest);
        }
        else {
            appointmentPage = appointmentRepository.findAllAppointmentForProject(projectId, pageRequest);
        }
        return PageableResponse.<Appointment>builder()
                .elements(appointmentPage.getContent())
                .page(appointmentPage.getNumber())
                .size(appointmentPage.getSize())
                .totalPages(appointmentPage.getTotalPages())
                .totalElements(appointmentPage.getTotalElements())
                .build();
    }

    public Appointment getById(UUID appointmentId) throws Exception{
        return appointmentRepository.findById(appointmentId).orElseThrow(() -> new EntityNotFoundException("appointment not found"));
    }

    private boolean isAppointmentOverlapping(UUID userId, UUID projectId, UUID itemId,
                                             LocalDateTime startDateTime, LocalDateTime endDateTime, UUID excludedId) throws Exception {

        List<Appointment> appointments = appointmentRepository.findOverlappingAppointmentsForProject(projectId, startDateTime, endDateTime);
        if (appointments.isEmpty()) {
            return false;
        }
        if(excludedId != null){
            appointments = appointments.stream().filter(a -> !a.getId().equals(excludedId)).toList();
        }
        Project project = projectService.getById(projectId);
        boolean userShared = project.isUserSharedCalendar();
        boolean itemShared = project.isItemSharedCalendar();

        if (!userShared && !itemShared) {
            return true;
        }
        if (userShared && itemShared) {
            return false;
        }
        if (userShared) {
            return isUserOverlapping(userId, appointments);
        }
        return isItemOverlapping(itemId, appointments);
    }

    private boolean isUserOverlapping(UUID userId, List<Appointment> appointments){
       return appointments.stream().anyMatch(a -> a.getUserProject().getUser().getId().equals(userId));
    }

    private boolean isItemOverlapping(UUID itemId, List<Appointment> appointments){
        if(itemId != null) {
            return appointments.stream().filter(a -> a.getItem() != null).anyMatch(a -> a.getItem().getId().equals(itemId));
        }
        else {
            return appointments.stream().anyMatch(a -> a.getItem() == null);
        }
    }

}
