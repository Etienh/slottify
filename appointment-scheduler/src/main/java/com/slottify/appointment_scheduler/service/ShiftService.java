package com.slottify.appointment_scheduler.service;

import com.slottify.appointment_scheduler.dto.CreateShiftRequest;
import com.slottify.appointment_scheduler.dto.PageableResponse;
import com.slottify.appointment_scheduler.dto.UpdateShiftRequest;
import com.slottify.appointment_scheduler.entity.Shift;
import com.slottify.appointment_scheduler.entity.UserProject;
import com.slottify.appointment_scheduler.exceptions.BadRequestException;
import com.slottify.appointment_scheduler.mapper.ShiftMapper;
import com.slottify.appointment_scheduler.repository.ShiftRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final ShiftMapper shiftMapper;
    private final UserProjectService userProjectService;

    public void create(UUID projectId, CreateShiftRequest request) throws Exception{
        UserProject userProject = userProjectService.getByProjectForUserInSession(projectId);
        if(shiftRepository.isShiftNameAlreadyPresentForProject(projectId, request.getName())){
            throw new BadRequestException("shift with name: " + request.getName() + " already exists for project");
        }
        Shift entityToSave = shiftMapper.toEntity(userProject, request);
        shiftRepository.save(entityToSave);
    }

    public void update(UUID shiftId, UpdateShiftRequest request) throws Exception{
        Shift shift = shiftRepository.findById(shiftId).orElseThrow(() -> new EntityNotFoundException("shift not found"));
        Shift updatedShift = shiftMapper.updateShift(request, shift);
        shiftRepository.save(updatedShift);
    }

    public PageableResponse<Shift> getAll(int page, int size, UUID projectId){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Shift> shiftPage = shiftRepository.findAllShiftForProject(projectId, pageRequest);
        return PageableResponse.<Shift>builder().
                elements(shiftPage.getContent())
                .page(shiftPage.getNumber())
                .size(shiftPage.getSize())
                .totalPages(shiftPage.getTotalPages())
                .totalElements(shiftPage.getTotalElements())
                .build();
    }

    public Shift getById(UUID shiftId) throws Exception{
        return shiftRepository.findById(shiftId).orElseThrow(() -> new EntityNotFoundException("shift not found"));
    }
}
