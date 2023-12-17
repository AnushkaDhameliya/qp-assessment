package com.qpassessment.service;

import com.qpassessment.entities.Item;
import com.qpassessment.entities.UserOrder;
import com.qpassessment.repositories.UserOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.*;

@Service

public class UserOrderServiceImpl implements UserOrderService {

    @Autowired
    private UserOrderRepository userOrderRepository;

    @Autowired
    private InventoryService inventoryService;

    @Override
    public List<UserOrder> getUserOrder(Long purchaseId) {
        return userOrderRepository.findByPurchaseIdAndActive(purchaseId, true);
    }

    @Override
    public Map<Long, List<UserOrder>> getUserAllOrders(Long userId) {
        Map<Long, List<UserOrder>> userOrdersMap = new HashMap<>();
        List<UserOrder> userOrders = userOrderRepository.findUserOrders(userId);
        for (UserOrder userOrder : userOrders) {
            if (userOrder.getActive()) {
                if (userOrdersMap.containsKey(userOrder.getPurchaseId())) {
                    userOrdersMap.get(userOrder.getPurchaseId()).add(userOrder);
                } else {
                    List<UserOrder> userOrderList = new ArrayList<>();
                    userOrderList.add(userOrder);
                    userOrdersMap.put(userOrder.getPurchaseId(), userOrderList);
                }
            }
        }
        return userOrdersMap;
    }

    @Override
    public Map<Long, List<UserOrder>> getAllUserOrders() {
        Map<Long, List<UserOrder>> userOrdersMap = new HashMap<>();
        List<UserOrder> userOrders = userOrderRepository.findAll();
        for (UserOrder userOrder : userOrders) {
            if (userOrdersMap.containsKey(userOrder.getPurchaseId())) {
                userOrdersMap.get(userOrder.getPurchaseId()).add(userOrder);
            } else {
                List<UserOrder> userOrderList = new ArrayList<>();
                userOrderList.add(userOrder);
                userOrdersMap.put(userOrder.getPurchaseId(), userOrderList);
            }
        }
        return userOrdersMap;
    }

    @Override
    public List<UserOrder> createUserOrder(List<UserOrder> userOrders) {
        if(!userOrders.isEmpty()){
            inventoryService.manageInventory(userOrders);
            Long purchaseId = userOrderRepository.getPurchaseId();
            for (UserOrder userOrder : userOrders) {
                if(userOrder.getItem().getSellingType().equals(Item.SellingType.QUANTITY)){
                    userOrder.setWeight(null);
                }
                else if(userOrder.getItem().getSellingType().equals(Item.SellingType.WEIGHT)){
                    userOrder.setQuantity(null);
                }
                userOrder.setPurchaseId(purchaseId);
                userOrder.setOrderedDate(new Date(Calendar.getInstance().getTimeInMillis()));
                userOrder.setActive(true);
                userOrderRepository.saveAndFlush(userOrder);
            }
        }

        return userOrders;
    }


    @Override
    public void deleteUserOrder(Long purchaseId) {
        List<UserOrder> orders = userOrderRepository.findByPurchaseId(purchaseId);
        for (UserOrder order : orders) {
            order.setActive(false);
            userOrderRepository.saveAndFlush(order);
        }
    }

    @Override
    public boolean validateUserOrder(List<UserOrder> userOrders) {
        for(UserOrder userOrder:userOrders){
            if(!inventoryService.validateUserOrder(userOrder)){
                return false;
            }
        }
        return true;
    }
}
