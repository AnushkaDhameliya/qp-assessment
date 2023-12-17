package com.qpassessment.service;

import com.qpassessment.entities.Inventory;
import com.qpassessment.entities.Item;
import com.qpassessment.entities.UserOrder;
import com.qpassessment.repositories.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InventoryServiceImpl implements InventoryService {

    public enum InventoryLevels {
        DANGER, MINIMUM, AVERAGE, MAXIMUM;
    }

    @Autowired
    private  InventoryRepository inventoryRepository;

    @Override
    public Inventory createInventory(Inventory inventory) {
        Inventory existingInventory=inventoryRepository.findByItemIdAndStockedDate(inventory.getItem().getId(),inventory.getStockedDate());
        if(existingInventory==null)
            return inventoryRepository.saveAndFlush(inventory);
        return null;
    }

    @Override
    public Inventory updateInventory(Inventory inventory) {
        Inventory existingInventory=inventoryRepository.findByItemIdAndStockedDate(inventory.getItem().getId(),inventory.getStockedDate());
        if(existingInventory!=null && existingInventory.getId().equals(inventory.getId()))
            return inventoryRepository.saveAndFlush(inventory);
        return null;
    }

    @Override
    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }

    @Override
    public Optional<Inventory> getInventory(Long id) {
        Optional<Inventory> inventory = inventoryRepository.findById(id);
        if (inventory.isPresent()) {
            return inventory;
        }
        return Optional.empty();
    }

    @Override
    public List<Inventory> getInventories() {
        return inventoryRepository.findAll();
    }

    @Override
    public Map<String, Set<Inventory>> getInventoryLevels() {

        Map<String, Set<Inventory>> inventoryMap = new HashMap<>();
        for (InventoryLevels level : InventoryLevels.values()) {
            inventoryMap.put(level.name(), new HashSet<Inventory>());
        }

        List<Inventory> inventories = getInventories();
        for (Inventory inventory : inventories) {
            if(inventory.getItem().getSellingType().equals(Item.SellingType.QUANTITY)){
                if (inventory.getQuantity() < 5) {
                    inventoryMap.get(InventoryLevels.DANGER.name()).add(inventory);
                } else if (inventory.getQuantity() >= 5 && inventory.getQuantity() < 20) {
                    inventoryMap.get(InventoryLevels.MINIMUM.name()).add(inventory);
                } else if (inventory.getQuantity() >= 20 && inventory.getQuantity() < 40) {
                    inventoryMap.get(InventoryLevels.AVERAGE.name()).add(inventory);
                } else if (inventory.getQuantity() >= 40.0d ) {
                    inventoryMap.get(InventoryLevels.MAXIMUM.name()).add(inventory);
                }
            }
            else if(inventory.getItem().getSellingType().equals(Item.SellingType.WEIGHT)){
                if (inventory.getWeight() < 5.0d) {
                    inventoryMap.get(InventoryLevels.DANGER.name()).add(inventory);
                } else if (inventory.getWeight() >= 5.0d && inventory.getWeight() < 20.0d) {
                    inventoryMap.get(InventoryLevels.MINIMUM.name()).add(inventory);
                } else if (inventory.getWeight() >= 20.0d && inventory.getWeight() < 40.0d) {
                    inventoryMap.get(InventoryLevels.AVERAGE.name()).add(inventory);
                } else if (inventory.getWeight() > 40.0d) {
                    inventoryMap.get(InventoryLevels.MAXIMUM.name()).add(inventory);
                }
            }
        }
        return inventoryMap;
    }

    @Override
    public void manageInventory(List<UserOrder> userOrders) {
        List<UserOrder> orderList = new ArrayList<>(userOrders);
        for(UserOrder order:orderList){
            Item item=order.getItem();
            List<Inventory> inventories=inventoryRepository.findByItemIdOrderByStockedDate(item.getId());
            for(Inventory inventory: inventories){
                if(item.getSellingType().equals(Item.SellingType.QUANTITY)){
                    if(inventory.getQuantity()-order.getQuantity()>=0){
                        inventory.setQuantity(inventory.getQuantity()-order.getQuantity());
                        break;
                    }
                    else{
                        inventory.setQuantity(0);
                        order.setQuantity(order.getQuantity()-inventory.getQuantity());
                    }

                } else if (item.getSellingType().equals(Item.SellingType.WEIGHT)) {

                    if(inventory.getWeight()-order.getWeight()>=0){
                        inventory.setWeight(inventory.getWeight()-order.getWeight());
                        break;
                    }
                    else{
                        inventory.setWeight(0.0d);
                        order.setWeight(order.getWeight()-inventory.getWeight());
                    }
                }
                inventoryRepository.saveAndFlush(inventory);
            }
        }
    }

    @Override
    public boolean validateUserOrder(UserOrder userOrder) {
        Item item=userOrder.getItem();
        List<Inventory> inventories=inventoryRepository.findByItemIdOrderByStockedDate(item.getId());
        if(item.getSellingType().equals(Item.SellingType.WEIGHT)){
            double totalWeight=0;
            for(Inventory inventory:inventories){
                if(inventory.getWeight()!=null)
                    totalWeight+=inventory.getWeight().doubleValue();
                if(totalWeight>=userOrder.getWeight()){
                    return true;
                }
            }
        }
        else if(item.getSellingType().equals(Item.SellingType.QUANTITY)){
            long totalQuantity=0;
            for(Inventory inventory:inventories){
                if(inventory.getQuantity()!=null)
                    totalQuantity+=inventory.getQuantity().longValue();
                if(totalQuantity>=userOrder.getQuantity()){
                    return true;
                }
            }
        }

        return false;
    }
}
