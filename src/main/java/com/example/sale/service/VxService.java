package com.example.sale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sale.dto.VxDTO;
import com.example.sale.entity.VxEntity;
import com.example.sale.vo.VxVO;

import java.util.List;
import java.util.Map;

/**
 * @author yilin
 * @date 2023-08-10 00:14:15
 */
public interface VxService extends IService<VxEntity> {

    List<VxVO> getData();

    void add(VxDTO vxDTO);
}

