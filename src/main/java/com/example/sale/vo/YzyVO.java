package com.example.sale.vo;

import com.example.sale.entity.YzyEntity;
import lombok.Data;

@Data
public class YzyVO {

    private Integer id;

    /**
     * 名字
     */
    private String name;

    /**
     * 销量
     */
    private Integer sale;

    private String cut;
}
