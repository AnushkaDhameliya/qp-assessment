package com.qpassessment.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class ItemDto {
    public enum Category{
        DAIRY,FRUITS,VEGETABLES,BEVERAGES;
    }

    public enum SellingType{
        WEIGHT,QUANTITY;
    }
    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    private String description;

    @PositiveOrZero
    private double price;

    @NotNull
    private Category category;

    @NotNull
    private SellingType sellingType;

    private Timestamp createdDate;

    private Timestamp modifiedDate;

}
