package com.example.sale.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataMulVO {
    private String name;

    private List<Object> stocksMS;

    private List<Integer> changeMS;

    private List<Map<String,Object>> historyMS;

    private List<Object> stocksSN;

    private List<Integer> changeSN;

    private List<Map<String,Object>> historySN;


    public DataMulVO(String name, List<Object> stocksMS,List<Object> stocksSN) {
        this.name = name;
        this.stocksMS = stocksMS;
        this.stocksSN = stocksSN;
        this.changeMS = new ArrayList<>();
        this.changeSN = new ArrayList<>();

    }
}
