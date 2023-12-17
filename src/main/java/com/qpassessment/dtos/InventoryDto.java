package com.qpassessment.dtos;

import com.qpassessment.entities.Item;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class InventoryDto {

    private Long id;

    @NotNull
    private Item item;

    @PositiveOrZero
    private Integer quantity;

    @PositiveOrZero
    private Double weight;

    @NotNull
    @NotEmpty
    private Date stockedDate;

    private Date expirationDate;

    private Timestamp createdDate;

    private Timestamp modifiedDate;
}
