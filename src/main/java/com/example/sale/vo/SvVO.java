package com.example.sale.vo;

import lombok.Data;

import java.util.List;

@Data
public class SvVO {

    private Integer id;

    private Integer sale;

    private String name;

    private String num;

    private List<Integer> change;
}
