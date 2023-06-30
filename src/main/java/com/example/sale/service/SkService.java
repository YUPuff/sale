package com.example.sale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sale.dto.SkDTO;
import com.example.sale.entity.SkEntity;
import com.example.sale.vo.SkVO;

import java.util.List;
import java.util.Map;

/**
 * @author yilin
 * @date 2023-06-25 15:57:08
 */
public interface SkService extends IService<SkEntity> {

    List<SkVO> getData(Integer target);

    List<SkVO> getData1(Integer role);

    void edit(SkDTO skDTO);
}

