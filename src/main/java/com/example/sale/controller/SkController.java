package com.example.sale.controller;

import com.example.sale.common.Result;
import com.example.sale.dto.SkDTO;
import com.example.sale.model.UserThreadLocal;
import com.example.sale.vo.UserVO;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.sale.service.SkService;


/**
 * @author yilin
 * @date 2023-06-25 15:57:08
 */
@RestController
@RequestMapping("sk")
public class SkController {
    @Autowired
    private SkService skService;

    @GetMapping("/getData")
    @RequiresRoles(logical = Logical.OR, value = {"ADMIN","SK","IT1"})
    public Result getData(){
        UserVO userVO = UserThreadLocal.get();
        Integer role = userVO.getRole();
        if (role>3)
            return Result.success(skService.getData1(role));
        return Result.success(skService.getData(role==0 ? null : userVO.getTarget()));
    }

    @PostMapping("/edit")
    @RequiresRoles(value = {"ADMIN"})
    public Result edit(@Validated @RequestBody SkDTO skDTO){
        skService.edit(skDTO);
        return Result.success();
    }

    @GetMapping("/info/{id}")
    @RequiresRoles(value = {"ADMIN"})
    public Result info(@PathVariable("id")Integer id){
        return Result.success(skService.getById(id));
    }
}
