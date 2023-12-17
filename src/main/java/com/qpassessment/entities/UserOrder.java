package com.qpassessment.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "USER_ORDER")
public class UserOrder {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "ORDER_ID_SEQ")
    @SequenceGenerator(name="ORDER_ID_SEQ", sequenceName = "ORDER_ID_SEQ", allocationSize=1)
    private Long id;

    @Column(name = "PURCHASE_ID")
    private Long purchaseId;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "WEIGHT")
    private Double weight;

    @Column(name = "ACTIVE")
    private Boolean active;

    @Column(name = "ORDERED_DATE")
    private Date orderedDate;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserDetail userDetail;
}
