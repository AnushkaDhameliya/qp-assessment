package com.qpassessment.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOrderDto {

    private Long id;

    private Long purchaseId;

    @PositiveOrZero
    private Integer quantity;

    @PositiveOrZero
    private Double weight;

    @NotNull
    private Date orderedDate;

    @NotNull
    private ItemDto item;

    @NotNull
    private UserDetailDto userDetail;
}
