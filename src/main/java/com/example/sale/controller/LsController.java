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

import com.example.sale.service.LsService;


@RestController
@RequestMapping("ls")
public class LsController {
    @Autowired
    private LsService lsService;

    @GetMapping("/getData")
    @RequiresRoles(logical = Logical.OR, value = {"ADMIN","LS"})
    public Result getData(){
        UserVO userVO = UserThreadLocal.get();
        return Result.success(lsService.getData(userVO.getRole()==0 ? null : userVO.getTarget()));
    }

}
