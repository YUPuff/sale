package com.example.sale.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonSale {

    private String name;

    private String monthSale;

    private String soldNum;
}
