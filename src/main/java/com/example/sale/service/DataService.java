package com.example.sale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sale.dto.DataDTO;
import com.example.sale.entity.DataEntity;
import com.example.sale.vo.DataVO;

import java.util.List;
import java.util.Map;

/**
 * @author yilin
 * @date 2023-08-02 10:46:35
 */
public interface DataService extends IService<DataEntity> {

    List<DataVO> getData(DataDTO dataDTO);

}

