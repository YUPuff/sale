package com.example.sale.controller;

import java.util.Arrays;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sale.entity.UserEntity;
import com.example.sale.service.UserService;



/**
 * @author yilin
 * @date 2023-06-19 15:50:56
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    

}
