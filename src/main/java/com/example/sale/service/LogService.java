package com.example.sale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sale.entity.LogEntity;
import com.example.sale.vo.LogVO;

import java.util.List;
import java.util.Map;

/**
 * @author yilin
 * @date 2023-08-05 12:44:25
 */
public interface LogService extends IService<LogEntity> {

    List<LogVO> getData();
}

