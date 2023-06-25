package com.example.sale.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
public class SkDTO {

    @NotNull(message = "id不能为空")
    private Integer id;

    @NotNull(message = "微店库存不能为空")
    @PositiveOrZero(message = "微店库存只能是不小于0的正整数")
    private Integer stockVd;

    @NotNull(message = "KMS库存不能为空")
    @PositiveOrZero(message = "KMS库存只能是不小于0的正整数")
    private Integer stockKms;


}
