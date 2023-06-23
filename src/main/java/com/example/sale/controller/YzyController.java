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
    @RequiresRoles(logical = Logical.OR, value = {"ADMIN","YZY","EN1"})
    public Result getData(){
        UserVO userVO = UserThreadLocal.get();
        Integer role = userVO.getRole();
        if (role == 5)
            return Result.success(yzyService.getData1(role));
        return Result.success(yzyService.getData(role==0 ? null : userVO.getTarget()));
    }

    @PostMapping("/edit")
    @RequiresRoles(value = {"ADMIN"})
    public Result edit(@Validated @RequestBody CommonDTO commonDTO){
        yzyService.edit(commonDTO);
        return Result.success();
    }


}
