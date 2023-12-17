package com.qpassessment.service;

import com.qpassessment.entities.Inventory;
import com.qpassessment.entities.UserOrder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface InventoryService {

    public Inventory createInventory(Inventory inventory);

    public Inventory updateInventory(Inventory inventory);

    public void deleteInventory(Long id);

    public Optional<Inventory> getInventory(Long id);

    public List<Inventory> getInventories();

    public Map<String, Set<Inventory>> getInventoryLevels();

    public void manageInventory(List<UserOrder> userOrders);

    public boolean validateUserOrder(UserOrder userOrder);
}
