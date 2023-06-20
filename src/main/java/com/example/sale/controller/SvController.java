package com.example.sale.controller;



import com.example.sale.common.Result;
import com.example.sale.model.UserThreadLocal;
import com.example.sale.vo.UserVO;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
