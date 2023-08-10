package com.example.sale.controller;



import com.example.sale.common.Result;
import com.example.sale.dto.VxDTO;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sale.service.VxService;



/**
 * @author yilin
 * @date 2023-08-10 00:14:15
 */
@RestController
@RequestMapping("vx")
public class VxController {
    @Autowired
    private VxService vxService;

    @RequestMapping("/getData")
    @RequiresRoles(value = {"ADMIN"})
    public Result getData(){
        return Result.success(vxService.getData());
    }

    @RequestMapping("/add")
    @RequiresRoles(value = {"ADMIN"})
    public Result add(@Validated @RequestBody VxDTO vxDTO){
        vxService.add(vxDTO);
        return Result.success();
    }

}
