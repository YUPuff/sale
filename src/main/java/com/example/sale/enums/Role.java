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

    IT3(6,"it3","IT3"),

    IT4(7,"it4","IT4"),

    IT5(8,"it5","IT5"),

    SK4(9,"sk4","SK4"),

    SK5(10,"sk5","SK5"),

    SK6(11,"sk6","SK6"),

    SK7(12,"sk7","SK7"),

    DINO(13,"dino","DINO");


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

