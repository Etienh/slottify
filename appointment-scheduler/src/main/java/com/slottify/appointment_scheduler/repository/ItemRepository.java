package com.slottify.appointment_scheduler.repository;

import com.slottify.appointment_scheduler.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {

    @Query(value = "SELECT i FROM Item i JOIN Project p ON i.project = p WHERE p.id = :projectId")
    Page<Item> findAllItemsForProject(@Param("projectId") UUID projectId, Pageable pageable);

    @Query(value = "SELECT COUNT(i) > 0 FROM Item i WHERE i.project.id = :projectId AND i.name = :itemName")
    boolean isItemNameAlreadyPresentForProject(@Param("projectId") UUID projectId, @Param("itemName") String itemName);
}
