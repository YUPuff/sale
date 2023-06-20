package com.example.sale.controller;



import com.example.sale.common.Result;
import com.example.sale.model.UserThreadLocal;
import com.example.sale.vo.UserVO;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sale.service.YzyService;



/**
 * @author yilin
 * @date 2023-06-19 15:50:56
 */
@RestController
@RequestMapping("yzy")
@CrossOrigin
public class YzyController {
    @Autowired
    private YzyService yzyService;

    @GetMapping("/getData")
    @RequiresRoles(logical = Logical.OR, value = {"ADMIN","YZY"})
    public Result getData(){
        UserVO userVO = UserThreadLocal.get();
        return Result.success(yzyService.getData(userVO.getRole()==0 ? null : userVO.getTarget()));
    }

}
