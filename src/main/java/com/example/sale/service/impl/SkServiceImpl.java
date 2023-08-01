package com.example.sale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sale.common.BusinessException;
import com.example.sale.constant.ResultConstants;
import com.example.sale.dto.SkDTO;
import com.example.sale.vo.SkVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.sale.dao.SkDao;
import com.example.sale.entity.SkEntity;
import com.example.sale.service.SkService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service("skService")
public class SkServiceImpl extends ServiceImpl<SkDao, SkEntity> implements SkService, ResultConstants {

    @Autowired
    private SkDao skDao;

    @Override
    public List<SkVO> getData(Integer target) {
        List<SkEntity> list = skDao.selectList(new LambdaQueryWrapper<SkEntity>().eq(target!=null,SkEntity::getId,target));
        List<SkVO> res = new ArrayList<>();
        for (SkEntity entity:list){
            res.add(generateVO(entity));
        }
        return res;
    }

    @Override
    public List<SkVO> getData1(Integer role) {
        List<Integer> ids = new ArrayList<>();
        List<SkVO> res = new ArrayList<>();
        switch (role){
            case 9:
                ids = Arrays.asList(9);
                break;
            default:
                ids = Arrays.asList(10);
        }
        for (Integer id : ids) {
            SkEntity entity = skDao.selectById(id);
            res.add(generateVO(entity));
        }
        return res;
    }

    @Override
    @Transactional
    public void edit(SkDTO skDTO) {
        SkEntity entity = skDao.selectById(skDTO.getId());
        if (entity == null)
            throw new BusinessException(NOT_EXIST);
        entity.setSaleVd(skDTO.getSaleVd());
        entity.setSaleKms(skDTO.getSaleKms());
        skDao.updateById(entity);
    }

    private SkVO generateVO(SkEntity entity){
        SkVO skVO = new SkVO();
        BeanUtils.copyProperties(entity,skVO);
        Integer total = entity.getSaleVd()+entity.getSaleKms();
        skVO.setSaleTotal(total);
        Integer a = total/entity.getBig();
        Integer b = total/entity.getSmall();
        skVO.setCut(a+"~"+b);
        return skVO;
    }
}