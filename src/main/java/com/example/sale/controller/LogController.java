package com.example.sale.controller;



import com.example.sale.common.Result;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sale.service.LogService;



/**
 * @author yilin
 * @date 2023-08-05 12:44:25
 */
@RestController
@RequestMapping("log")
public class LogController {
    @Autowired
    private LogService logService;

    @RequestMapping("/getData")
    @RequiresRoles(value = {"ADMIN"})
    public Result getData(){
        return Result.success(logService.getData());
    }
}
