package com.example.sale.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
public class CommonDTO {

    @NotNull(message = "id不能为空")
    private Integer id;

    @NotNull(message = "销量不能为空")
    @PositiveOrZero(message = "销量只能是不小于0的正整数")
    private Integer sale;
}
