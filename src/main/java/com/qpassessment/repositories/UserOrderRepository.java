package com.qpassessment.repositories;

import com.qpassessment.entities.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrderRepository extends JpaRepository<UserOrder,Long> {

    List<UserOrder> findByPurchaseIdAndActive(Long purchaseId,Boolean active);

    List<UserOrder> findByPurchaseId(Long purchaseId);

    @Query("SELECT u FROM UserOrder u WHERE userDetail.userId=:userId")
    List<UserOrder> findUserOrders(@Param("userId") Long userId);

    @Query(value = "SELECT NEXTVAL('ORDER_PURCHASE_ID_SEQ')")
    Long getPurchaseId();
}
