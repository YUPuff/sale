package com.example.sale.vo;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class DataVO {
    private String name;

    private List<Object> stocks;

//    private Map<String,Integer> change;

    private int[] change;

    private List<Map<String,Object>> history;

    public DataVO(String name, List<Object> stocks) {
        this.name = name;
        this.stocks = stocks;
        this.change = new int[10];
    }
}
