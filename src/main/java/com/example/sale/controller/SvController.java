package com.example.sale.controller;



import com.example.sale.common.Result;
import com.example.sale.dto.CommonDTO;
import com.example.sale.model.UserThreadLocal;
import com.example.sale.vo.UserVO;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.sale.service.SvService;


@RestController
@RequestMapping("sv")
public class SvController {
    @Autowired
    private SvService svService;

    @GetMapping("/getData")
    @RequiresRoles(logical = Logical.OR, value = {"ADMIN","SV"})
    public Result getData(){
        UserVO userVO = UserThreadLocal.get();
        return Result.success(svService.getData(userVO.getRole()==0 ? null : userVO.getTarget()));
    }

    @PostMapping("/edit")
    @RequiresRoles(value = {"ADMIN"})
    public Result edit(@Validated @RequestBody CommonDTO commonDTO){
        svService.edit(commonDTO);
        return Result.success();
    }

    @GetMapping("/info/{id}")
    @RequiresRoles(value = {"ADMIN"})
    public Result info(@PathVariable("id")Integer id){
        return Result.success(svService.getById(id));
    }
}
