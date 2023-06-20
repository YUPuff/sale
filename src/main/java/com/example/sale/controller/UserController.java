package com.example.sale.controller;



import com.example.sale.annotation.NoAuth;
import com.example.sale.common.Result;
import com.example.sale.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.sale.service.UserService;



/**
 * @author yilin
 * @date 2023-06-19 15:50:56
 */
@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @NoAuth
    public Result login(@RequestBody UserDTO userDTO){
        return Result.success(userService.login(userDTO));
    }

    @GetMapping("/info")
    @NoAuth
    public Result info(@RequestParam String token){
        return Result.success(userService.getUserByToken(token));
    }

    @RequestMapping("/logout")
    public Result logout(@RequestHeader("token")String token){
        userService.logout(token);
        return Result.success();
    }
}
