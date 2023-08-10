package com.example.sale.controller;



import com.example.sale.annotation.NoAuth;
import com.example.sale.common.Result;
import com.example.sale.dto.DataDTO;
import com.example.sale.dto.UserDTO;
import com.example.sale.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.sale.service.UserService;

import javax.servlet.http.HttpServletRequest;


/**
 * @author yilin
 * @date 2023-06-19 15:50:56
 */
@RestController
@Slf4j
@RequestMapping("user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DataService dataService;


    @PostMapping("/login")
    @NoAuth
    public Result login(@RequestBody UserDTO userDTO,HttpServletRequest request){

        return Result.success(userService.login(userDTO,request));
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

    @RequestMapping("/getDataVd")
    @RequiresRoles(value = {"ADMIN"})
    public Result getDataVd(){
        return Result.success(userService.getDataVd());
    }

    @RequestMapping("/getDataKMS")
    @RequiresRoles(value = {"ADMIN"})
    public Result getDataKMS(){
        return Result.success(userService.getDataKMS());
    }

    @RequestMapping("/getCountVd")
    @RequiresRoles(value = {"ADMIN"})
    public Result getCountVd(DataDTO dataDTO){
        return Result.success(dataService.getDataVd(dataDTO));
    }

    @RequestMapping("/getCountKMS")
    @RequiresRoles(value = {"ADMIN"})
    public Result getCountKMS(DataDTO dataDTO){
        return Result.success(dataService.getDataKMS(dataDTO));
    }
}
