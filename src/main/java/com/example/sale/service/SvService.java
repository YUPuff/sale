package com.example.sale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sale.dto.CommonDTO;
import com.example.sale.entity.SvEntity;
import com.example.sale.vo.SvVO;

import java.util.List;
import java.util.Map;

/**
 * @author yilin
 * @date 2023-06-20 18:56:19
 */
public interface SvService extends IService<SvEntity> {

    List<SvVO> getData(Integer target);

    void edit(CommonDTO commonDTO);
}

