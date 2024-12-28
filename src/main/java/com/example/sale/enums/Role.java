package com.example.sale.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    ADMIN(0,"管理员","ADMIN"),

    SK(1,"迷","SK"),

    YZY(2,"一直娱","YZY"),

    SV(3,"seventeen","SV"),

    SK4(9,"sk4","SK4"),

    LZM(4,"LZM","LZM"),

    HRJ(5,"HRJ","HRJ"),

    LDN(6,"LDN","LDN"),

    LKC(7,"LKC","LKC"),

    PZS(8,"PZS","PZS"),

    LMK(12,"LMK","LMK"),

    ZCL(13,"ZCL","ZCL"),

    JASMINE(11,"jas","JASMINE"),

//    CHERRY(12,"cherry","CHERRY"),
//
//    DINO(13,"dino","DINO"),

    ZBO2(14,"zbo2","ZBO2"),

    AE1(15,"ae1","AE1"),

    AE2(16,"ae2","AE2");


    private Integer code;

    private String name;

    private String key;

    public static String format(Integer code) {
        for (Role value : Role.values()) {
            if (value.code == code) {
                return value.key;
            }
        }
        return "";
    }

}

