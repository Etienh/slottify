package com.slottify.appointment_scheduler.service;

import com.slottify.appointment_scheduler.dto.CreateAppointmentRequest;
import com.slottify.appointment_scheduler.dto.PageableResponse;
import com.slottify.appointment_scheduler.dto.UpdateAppointmentRequest;
import com.slottify.appointment_scheduler.entity.*;
import com.slottify.appointment_scheduler.exceptions.BadRequestException;
import com.slottify.appointment_scheduler.mapper.AppointmentMapper;
import com.slottify.appointment_scheduler.repository.AppointmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private AppointmentMapper appointmentMapper;
    @Mock
    private ItemService itemService;
    @Mock
    private PriorityService priorityService;
    @Mock
    private UserProjectService userProjectService;
    @Mock
    private ProjectService projectService;

    @InjectMocks
    private AppointmentService appointmentService;

    private UUID userId;
    private UUID projectId;
    private UUID itemId;
    private UUID priorityId;
    private UUID appointmentId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        projectId = UUID.randomUUID();
        itemId = UUID.randomUUID();
        priorityId = UUID.randomUUID();
        appointmentId = UUID.randomUUID();
    }

    @Test
    void create_ShouldSaveAppointment() throws Exception {
        // given
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        CreateAppointmentRequest request = new CreateAppointmentRequest(start, end, "description", "12345");

        User user = User.builder().id(userId).build();
        UserProject userProject = UserProject.builder().id(UUID.randomUUID()).user(user).build();
        Item item = Item.builder().name("item").build();
        item.setId(itemId);
        Priority priority = Priority.builder().name("priority").level(1).build();
        priority.setId(priorityId);
        Appointment appointment = Appointment.builder().startDateTime(start).endDateTime(end).build();

        when(userProjectService.getByProjectAndUser(projectId, userId)).thenReturn(userProject);
        when(itemService.getById(itemId)).thenReturn(item);
        when(priorityService.getById(priorityId)).thenReturn(priority);
        when(appointmentRepository.findOverlappingAppointmentsForProject(projectId, start, end)).thenReturn(List.of());
        when(appointmentMapper.toEntity(userProject, request, item, priority)).thenReturn(appointment);

        // when
        appointmentService.create(userId, projectId, itemId, priorityId, request);

        // then
        verify(userProjectService).getByProjectAndUser(projectId, userId);
        verify(userProjectService, never()).getByProjectForUserInSession(any());
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void create_ShouldUseSessionUserProject() throws Exception {
        // given
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        CreateAppointmentRequest request = new CreateAppointmentRequest(start, end, "description", "12345");

        User user = User.builder().id(userId).build();
        UserProject userProject = UserProject.builder().id(UUID.randomUUID()).user(user).build();
        Appointment appointment = Appointment.builder().startDateTime(start).endDateTime(end).build();

        when(userProjectService.getByProjectForUserInSession(projectId)).thenReturn(userProject);
        when(appointmentRepository.findOverlappingAppointmentsForProject(projectId, start, end)).thenReturn(List.of());
        when(appointmentMapper.toEntity(userProject, request, null, null)).thenReturn(appointment);

        // when
        appointmentService.create(null, projectId, null, null, request);

        // then
        verify(userProjectService).getByProjectForUserInSession(projectId);
        verify(userProjectService, never()).getByProjectAndUser(any(), any());
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void create_ShouldThrowBadRequestException() throws Exception {
        // given
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        CreateAppointmentRequest request = new CreateAppointmentRequest(start, end, "description", "12345");

        User user = User.builder().id(userId).build();
        UserProject userProject = UserProject.builder().id(UUID.randomUUID()).user(user).build();
        Appointment overlappingAppointment = Appointment.builder().startDateTime(start).endDateTime(end).build();
        Project project = Project.builder().userSharedCalendar(false).itemSharedCalendar(false).build();

        when(userProjectService.getByProjectAndUser(projectId, userId)).thenReturn(userProject);
        when(appointmentRepository.findOverlappingAppointmentsForProject(projectId, start, end))
                .thenReturn(List.of(overlappingAppointment));
        when(projectService.getById(projectId)).thenReturn(project);

        // when / then
        assertThrows(BadRequestException.class,
                () -> appointmentService.create(userId, projectId, null, null, request));
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void update_ShouldSaveUpdatedAppointment() throws Exception {
        // given
        LocalDateTime newStart = LocalDateTime.now();
        LocalDateTime newEnd = newStart.plusHours(1);
        UpdateAppointmentRequest request = new UpdateAppointmentRequest(newStart, newEnd, "new description", "6789");

        User user = User.builder().id(userId).build();
        UserProject userProject = UserProject.builder().id(UUID.randomUUID()).user(user).build();
        Item item = Item.builder().name("item").build();
        item.setId(itemId);

        Appointment existingAppointment = Appointment.builder().build();
        existingAppointment.setId(appointmentId);

        Appointment updatedAppointment = Appointment.builder()
                .startDateTime(newStart)
                .endDateTime(newEnd)
                .userProject(userProject)
                .item(item)
                .build();
        updatedAppointment.setId(appointmentId);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(existingAppointment));
        when(appointmentMapper.updateAppointment(request, existingAppointment)).thenReturn(updatedAppointment);
        when(appointmentRepository.findOverlappingAppointmentsForProject(projectId, newStart, newEnd)).thenReturn(List.of());

        // when
        appointmentService.update(null, projectId, null, null, appointmentId, request);

        // then
        verify(userProjectService, never()).getByProjectAndUser(any(), any());
        verify(appointmentRepository).save(updatedAppointment);
    }

    @Test
    void update_ShouldReassignUserProjectItemAndPriority() throws Exception {
        // given
        UpdateAppointmentRequest request = new UpdateAppointmentRequest(null, null, "new description", "6789");

        Appointment existingAppointment = Appointment.builder().build();
        existingAppointment.setId(appointmentId);

        Appointment updatedAppointment = Appointment.builder().build();
        updatedAppointment.setId(appointmentId);

        User newUser = User.builder().id(userId).build();
        UserProject newUserProject = UserProject.builder().id(UUID.randomUUID()).user(newUser).build();
        Item newItem = Item.builder().name("new item").build();
        newItem.setId(itemId);
        Priority newPriority = Priority.builder().name("new priority").level(2).build();
        newPriority.setId(priorityId);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(existingAppointment));
        when(appointmentMapper.updateAppointment(request, existingAppointment)).thenReturn(updatedAppointment);
        when(userProjectService.getByProjectAndUser(projectId, userId)).thenReturn(newUserProject);
        when(itemService.getById(itemId)).thenReturn(newItem);
        when(priorityService.getById(priorityId)).thenReturn(newPriority);

        // when
        appointmentService.update(userId, projectId, itemId, priorityId, appointmentId, request);

        // then
        ArgumentCaptor<Appointment> captor = ArgumentCaptor.forClass(Appointment.class);
        verify(appointmentRepository).save(captor.capture());
        assertEquals(newUserProject, captor.getValue().getUserProject());
        assertEquals(newItem, captor.getValue().getItem());
        assertEquals(newPriority, captor.getValue().getPriority());
    }

    @Test
    void update_ShouldThrowBadRequestException() throws Exception {
        // given
        LocalDateTime newStart = LocalDateTime.now();
        LocalDateTime newEnd = newStart.plusHours(1);
        UpdateAppointmentRequest request = new UpdateAppointmentRequest(newStart, newEnd, "new description", "6789");

        User user = User.builder().id(userId).build();
        UserProject userProject = UserProject.builder().id(UUID.randomUUID()).user(user).build();
        Item item = Item.builder().name("item").build();
        item.setId(itemId);

        Appointment existingAppointment = Appointment.builder().build();
        existingAppointment.setId(appointmentId);

        Appointment updatedAppointment = Appointment.builder()
                .startDateTime(newStart)
                .endDateTime(newEnd)
                .userProject(userProject)
                .item(item)
                .build();
        updatedAppointment.setId(appointmentId);

        Appointment anotherOverlappingAppointment = Appointment.builder().build();
        anotherOverlappingAppointment.setId(UUID.randomUUID());

        Project project = Project.builder().userSharedCalendar(false).itemSharedCalendar(false).build();

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(existingAppointment));
        when(appointmentMapper.updateAppointment(request, existingAppointment)).thenReturn(updatedAppointment);
        when(appointmentRepository.findOverlappingAppointmentsForProject(projectId, newStart, newEnd))
                .thenReturn(List.of(anotherOverlappingAppointment));
        when(projectService.getById(projectId)).thenReturn(project);

        // when / then
        assertThrows(BadRequestException.class,
                () -> appointmentService.update(null, projectId, null, null, appointmentId, request));
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void update_ShouldThrowEntityNotFoundException() {
        // given
        UpdateAppointmentRequest request = new UpdateAppointmentRequest(null, null, "new description", "6789");
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        // when / then
        assertThrows(EntityNotFoundException.class,
                () -> appointmentService.update(null, projectId, null, null, appointmentId, request));
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void unassignItem_ShouldRemoveItemAndSaveAppointment() {
        // given
        Item item = Item.builder().name("item").build();
        item.setId(itemId);
        Appointment appointment = Appointment.builder().item(item).build();
        appointment.setId(appointmentId);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        // when
        appointmentService.unassignItem(appointmentId);

        // then
        assertNull(appointment.getItem());
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void unassignItem_ShouldThrowEntityNotFoundException() {
        // given
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        // when / then
        assertThrows(EntityNotFoundException.class, () -> appointmentService.unassignItem(appointmentId));
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void unassignPriority_ShouldRemovePriorityAndSaveAppointment() {
        // given
        Priority priority = Priority.builder().name("priority").level(1).build();
        priority.setId(priorityId);
        Appointment appointment = Appointment.builder().priority(priority).build();
        appointment.setId(appointmentId);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        // when
        appointmentService.unassignPriority(appointmentId);

        // then
        assertNull(appointment.getPriority());
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void unassignPriority_ShouldThrowEntityNotFoundException() {
        // given
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        // when / then
        assertThrows(EntityNotFoundException.class, () -> appointmentService.unassignPriority(appointmentId));
        verify(appointmentRepository, never()).save(any());
    }

    @Test
    void getAllFiltered_ShouldReturnAppointmentsForUserProject() throws Exception {
        // given
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);

        User user = User.builder().id(userId).build();
        UserProject userProject = UserProject.builder().id(UUID.randomUUID()).user(user).build();
        Appointment appointment = Appointment.builder().build();
        appointment.setId(appointmentId);
        Page<Appointment> appointmentPage = new PageImpl<>(List.of(appointment), pageRequest, 1);

        when(userProjectService.getByProjectAndUser(projectId, userId)).thenReturn(userProject);
        when(appointmentRepository.findAllAppointmentForUserProject(eq(userProject.getId()), eq(pageRequest)))
                .thenReturn(appointmentPage);

        // when
        PageableResponse<Appointment> result = appointmentService.getAllFiltered(page, size, projectId, userId);

        // then
        assertEquals(List.of(appointment), result.getElements());
        assertEquals(1, result.getTotalElements());
        verify(appointmentRepository, never()).findAllAppointmentForProject(any(), any());
    }

    @Test
    void getAllFiltered_ShouldReturnAppointmentsForProject() throws Exception {
        // given
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);

        Appointment appointment = Appointment.builder().build();
        appointment.setId(appointmentId);
        Page<Appointment> appointmentPage = new PageImpl<>(List.of(appointment), pageRequest, 1);

        when(appointmentRepository.findAllAppointmentForProject(eq(projectId), eq(pageRequest)))
                .thenReturn(appointmentPage);

        // when
        PageableResponse<Appointment> result = appointmentService.getAllFiltered(page, size, projectId, null);

        // then
        assertEquals(List.of(appointment), result.getElements());
        assertEquals(1, result.getTotalElements());
        verify(userProjectService, never()).getByProjectAndUser(any(), any());
        verify(appointmentRepository, times(1)).findAllAppointmentForProject(projectId, pageRequest);
    }

    @Test
    void getById_ShouldReturnAppointment() throws Exception {
        // given
        Appointment appointment = Appointment.builder().build();
        appointment.setId(appointmentId);
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        // when
        Appointment result = appointmentService.getById(appointmentId);

        // then
        assertEquals(appointment, result);
    }

    @Test
    void getById_ShouldThrowEntityNotFoundException() {
        // given
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        // when / then
        assertThrows(EntityNotFoundException.class, () -> appointmentService.getById(appointmentId));
    }
}
