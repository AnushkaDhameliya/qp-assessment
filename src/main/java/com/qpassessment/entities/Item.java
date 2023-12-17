package com.qpassessment.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Set;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="ITEM")
public class Item {
    public enum Category{
        DAIRY,FRUITS,VEGETABLES,BEVERAGES;
    }

    public enum SellingType{
        WEIGHT,QUANTITY
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "ITEM_ID_SEQ")
    @SequenceGenerator(name="ITEM_ID_SEQ", sequenceName = "ITEM_ID_SEQ", allocationSize=1)
    @Column(name="ID")
    private Long id;

    @Column(name="NAME")
    private String name;

    @Column(name="DESCRIPTION")
    private String description;

    @Column(name="PRICE")
    private double price;

    @Column(name="CATEGORY")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name="SELLING_TYPE")
    @Enumerated(EnumType.STRING)
    private SellingType sellingType;

    @Column(name="CREATED_DATE")
    @CreationTimestamp
    private Timestamp createdDate;

    @Column(name="MODIFIED_DATE")
    @UpdateTimestamp
    private Timestamp modifiedDate;

    @OneToMany
    @JoinColumn(name = "ITEM_ID")
    private Set<Inventory> inventorySet;

//    @OneToMany
//    @JoinColumn(name = "ITEM_ID")
//    private Set<UserOrder> userOrdersSet;
}
