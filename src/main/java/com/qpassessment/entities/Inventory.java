package com.qpassessment.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "INVENTORY")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "INVENTORY_ID_SEQ")
    @SequenceGenerator(name="INVENTORY_ID_SEQ", sequenceName = "INVENTORY_ID_SEQ", allocationSize=1)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "WEIGHT")
    private Double weight;

    @Column(name = "STOCKED_DATE",nullable = false)
    private Date stockedDate;

    @Column(name = "EXPIRATION_DATE")
    private Date expirationDate;

    @Column(name = "CREATED_DATE")
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name = "MODIFIED_DATE")
    @UpdateTimestamp
    private Timestamp modifiedDate;

}
