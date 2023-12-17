package com.qpassessment.service;

import com.qpassessment.entities.UserOrder;

import java.util.List;
import java.util.Map;

public interface UserOrderService {

    List<UserOrder> getUserOrder(Long purchaseId);

    Map<Long,List<UserOrder>> getUserAllOrders(Long userId);

    Map<Long,List<UserOrder>> getAllUserOrders();

    List<UserOrder> createUserOrder(List<UserOrder> userOrders);
    void deleteUserOrder(Long purchaseId);

    boolean validateUserOrder(List<UserOrder> userOrders);
}
