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

    RIIZE(4,"riize","RIIZE"),

    IT2(5,"it2","IT2"),

    ZB1(6,"zb1","ZB1"),

    BROWN(7,"brown","BROWN"),

    DREAM(8,"dream","DREAM"),

    SK4(9,"sk4","SK4"),

    SK5(10,"sk5","SK5"),

    JASMINE(11,"jas","JASMINE"),

    NCT3(12,"nct3","NCT3"),

    DINO(13,"dino","DINO"),

    ZBO2(14,"zbo2","ZBO2"),

    WAYV1(15,"wayv1","WAYV1"),

    SK1(16,"sk1","SK1"),

    SK2(17,"sk2","SK2");


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

