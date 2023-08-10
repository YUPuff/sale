package com.example.sale.vo;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VxVO {
    private String name;

    private List<Object> vxs;
}
