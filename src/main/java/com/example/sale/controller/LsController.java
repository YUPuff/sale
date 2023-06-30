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

import com.example.sale.service.LsService;


@RestController
@RequestMapping("ls")
public class LsController {
    @Autowired
    private LsService lsService;

    @GetMapping("/getData")
    @RequiresRoles(logical = Logical.OR, value = {"ADMIN","LS","SK1"})
    public Result getData(){
        UserVO userVO = UserThreadLocal.get();
        Integer role = userVO.getRole();
        if (role == 6)
            return Result.success(lsService.getData1(role));
        return Result.success(lsService.getData(role==0 ? null : userVO.getTarget()));
    }

    @PostMapping("/edit")
    @RequiresRoles(value = {"ADMIN"})
    public Result edit(@Validated @RequestBody CommonDTO commonDTO){
        lsService.edit(commonDTO);
        return Result.success();
    }

}
