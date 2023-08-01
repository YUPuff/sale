package com.example.sale.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
public class SkDTO {

    @NotNull(message = "id不能为空")
    private Integer id;

    @NotNull(message = "微店销量不能为空")
    @PositiveOrZero(message = "微店销量只能是不小于0的正整数")
    private Integer saleVd;

    @NotNull(message = "KMS销量不能为空")
    @PositiveOrZero(message = "KMS销量只能是不小于0的正整数")
    private Integer saleKms;


}
