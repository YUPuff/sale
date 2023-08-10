package com.example.sale.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class VxDTO {

    @NotNull(message = "账号名不能为空")
    private String name;

    @NotNull(message = "微信号不能为空")
    private String vx;

    private String address;
}
