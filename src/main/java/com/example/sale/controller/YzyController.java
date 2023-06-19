package com.example.sale.controller;



import com.example.sale.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
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
public class YzyController {
    @Autowired
    private YzyService yzyService;

    @GetMapping("/getData")
    public Result getData(){
        return Result.success(yzyService.getData());
    }

}
