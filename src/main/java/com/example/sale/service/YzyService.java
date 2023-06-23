package com.example.sale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sale.dto.CommonDTO;
import com.example.sale.entity.YzyEntity;
import com.example.sale.vo.YzyVO;

import java.util.List;
import java.util.Map;

/**
 * @author yilin
 * @date 2023-06-19 15:50:56
 */
public interface YzyService extends IService<YzyEntity> {

    List<YzyVO> getData(Integer target);

    List<YzyVO> getData1(Integer role);

    void edit(CommonDTO commonDTO);
}

