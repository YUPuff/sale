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

    IT1(4,"it1","IT1"),

    IT2(5,"it2","IT2"),

    ZB1(6,"zb1","ZB1"),

    BROWN(7,"brown","BROWN"),

    NCT1(8,"nct1","NCT1"),

    SK4(9,"sk4","SK4"),

    SK5(10,"sk5","SK5"),

    NCT2(11,"nct2","NCT2"),

    NCT3(12,"nct3","NCT3"),

    DINO(13,"dino","DINO"),

    NCT4(14,"nct4","NCT4"),

    NCT5(15,"nct5","NCT5");


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

