package com.example.sale.model;

import com.example.sale.vo.UserVO;

/**
 * 确保用户信息线程隔离
 */

public class UserThreadLocal {

    private static final ThreadLocal<UserVO> USER = new ThreadLocal<>();

    public static void put(UserVO userVO){
        USER.set(userVO);
    }

    public static UserVO get(){
        return USER.get();
    }

    public static void remove(){
        USER.remove();
    }
}
