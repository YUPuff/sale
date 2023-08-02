package com.example.sale.controller;

import java.util.Arrays;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sale.entity.DataEntity;
import com.example.sale.service.DataService;



/**
 * @author yilin
 * @date 2023-08-02 10:46:35
 */
@RestController
@RequestMapping("Data")
public class DataController {
    @Autowired
    private DataService dataService;


}
