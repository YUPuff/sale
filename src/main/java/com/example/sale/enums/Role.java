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

    LS(4,"lessfirm","LS"),

    ND(5,"nd","ND"),

    SK1(6,"sk1","SK1"),

    SK2(7,"sk2","SK2"),

    SK3(8,"sk3","SK3"),

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

