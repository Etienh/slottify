package com.slottify.appointment_scheduler.service;

import com.slottify.appointment_scheduler.dto.CreateItemRequest;
import com.slottify.appointment_scheduler.dto.PageableResponse;
import com.slottify.appointment_scheduler.dto.UpdateItemRequest;
import com.slottify.appointment_scheduler.entity.Item;
import com.slottify.appointment_scheduler.entity.Project;
import com.slottify.appointment_scheduler.mapper.ItemMapper;
import com.slottify.appointment_scheduler.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final ProjectService projectService;

    public void create(UUID projectId, CreateItemRequest request) throws Exception{
        Project project = projectService.getById(projectId);
        Item entityToSave = itemMapper.toEntity(project, request);
        itemRepository.save(entityToSave);
    }

    public void update(UUID itemId, UpdateItemRequest request) throws Exception{
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("item not found"));
        Item updateItem = itemMapper.updateItem(request, item);
        itemRepository.save(updateItem);
    }

    public PageableResponse<Item> getAll(int page, int size, UUID projectId) throws Exception{
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Item> itemPage = itemRepository.findAllItemsForProject(projectId, pageRequest);
        return PageableResponse.<Item>builder()
                .elements(itemPage.getContent())
                .page(itemPage.getNumber())
                .size(itemPage.getSize())
                .totalPages(itemPage.getTotalPages())
                .totalElements(itemPage.getTotalElements())
                .build();

    }

    public Item getById(UUID itemId) throws Exception{
       return itemRepository.findById(itemId).orElseThrow(() -> new EntityNotFoundException("item not found"));
    }
}
