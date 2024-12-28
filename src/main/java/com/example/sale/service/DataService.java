package com.example.sale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sale.dto.DataDTO;
import com.example.sale.entity.DataEntity;
import com.example.sale.entity.DataKmsEntity;
import com.example.sale.vo.DataMulVO;
import com.example.sale.vo.DataVO;

import java.util.List;

/**
 * @author yilin
 * @date 2023-08-02 10:46:35
 */
public interface DataService extends IService<DataEntity> {

    List<DataVO> getDataVd(DataDTO dataDTO,List<String> names,Integer begin);

    List<DataVO> getDataKMS(DataDTO dataDTO,List<String> names,Integer begin);

    List<DataMulVO> getDataKMSSale(DataDTO dataDTO, List<String> names, Integer begin);

    List<DataEntity> searchVd(DataDTO dataDTO);

    List<DataKmsEntity> searchKMS(DataDTO dataDTO);




    DataVO getChangeVd(String name);

    DataVO getChangeKMS(String name);

}

