package com.example.sale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sale.entity.LsEntity;
import com.example.sale.vo.LsVO;

import java.util.List;
import java.util.Map;

/**
 * @author yilin
 * @date 2023-06-22 10:33:49
 */
public interface LsService extends IService<LsEntity> {

    List<LsVO> getData(Integer target);
}

