package com.example.sale.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IpDTO {

    private String country;

    private String regionName;

    public String generateRegion(){
        return country+"-"+regionName;
    }
}
