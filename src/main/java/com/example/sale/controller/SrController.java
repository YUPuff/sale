package com.example.sale.controller;



import com.example.sale.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sale.service.SrService;


@RestController
@RequestMapping("sr")
public class SrController {
    @Autowired
    private SrService srService;

    @GetMapping("/getData")
    public Result getData(){
        return Result.success(srService.getData());
    }
}
