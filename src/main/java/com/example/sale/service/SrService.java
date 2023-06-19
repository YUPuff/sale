package com.example.sale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sale.entity.SrEntity;
import com.example.sale.vo.SrVO;

import java.util.List;
import java.util.Map;

/**
 * @author yilin
 * @date 2023-06-19 15:50:56
 */
public interface SrService extends IService<SrEntity> {

    List<SrVO> getData();

}

