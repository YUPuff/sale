package com.example.sale.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class FourDTO {

    @NotBlank(message = "A不能为空")
    private String vd1;

    @NotBlank(message = "B不能为空")
    private String vd2;

    @NotBlank(message = "D不能为空")
    private String kms1;

    @NotBlank(message = "E不能为空")
    private String kms2;

}
