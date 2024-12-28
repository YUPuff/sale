package com.example.sale.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkVO {

    private Integer id;

    private String name;

    private Integer sale;

    private String cut;

    private List<Integer> change_vd;

    private List<Integer> change_kms;

    public SkVO(String name, Integer sale, String cut) {
        this.name = name;
        this.sale = sale;
        this.cut = cut;
    }
}
