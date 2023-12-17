package com.qpassessment.repositories;

import com.qpassessment.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    List<Inventory> findByItemIdOrderByStockedDate(Long itemId);

    Inventory findByItemIdAndStockedDate(Long itemId, Date stockedDate);
}
