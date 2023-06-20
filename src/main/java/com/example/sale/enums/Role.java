package com.example.sale.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    ADMIN(0,"管理员","ADMIN"),

    SR(1,"星河","SR"),

    YZY(2,"一直娱","YZY");


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
