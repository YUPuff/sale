package com.example.sale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sale.common.BusinessException;
import com.example.sale.constant.ResultConstants;
import com.example.sale.dto.CommonDTO;
import com.example.sale.vo.YzyVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.sale.dao.YzyDao;
import com.example.sale.entity.YzyEntity;
import com.example.sale.service.YzyService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service("yzyService")
public class YzyServiceImpl extends ServiceImpl<YzyDao, YzyEntity> implements YzyService, ResultConstants {

    @Autowired
    private YzyDao yzyDao;

    @Override
    public List<YzyVO> getData(Integer target) {
        List<YzyEntity> list = yzyDao.selectList(new LambdaQueryWrapper<YzyEntity>().eq(target!=null,YzyEntity::getId,target));
        List<YzyVO> res = new ArrayList<>();
        for (YzyEntity entity:list){
            YzyVO yzyVO = new YzyVO();
            BeanUtils.copyProperties(entity,yzyVO);
            yzyVO.setCut(generateCut(entity.getType(), entity.getSale()));
            res.add(yzyVO);
        }
        return res;
    }

    /**
     * 查询朴成训、沈载伦、李羲承、梁祯元、朴综星
     * @param role
     * @return
     */
    @Override
    public List<YzyVO> getData1(Integer role) {
        List<YzyVO> res = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        if (role == 5){
            list = Arrays.asList(8,9,10,11,13);
        }else if (role == 9){
            list = Arrays.asList(13);
        }
        for (Integer id : list) {
            YzyEntity entity = yzyDao.selectById(id);
            YzyVO yzyVO = new YzyVO();
            BeanUtils.copyProperties(entity,yzyVO);
            yzyVO.setCut(generateCut(entity.getType(),entity.getSale()));
            res.add(yzyVO);
        }
        return res;
    }

    @Override
    public void edit(CommonDTO commonDTO) {
        YzyEntity entity = yzyDao.selectById(commonDTO.getId());
        if (entity == null)
            throw new BusinessException(NOT_EXIST);
        entity.setSale(commonDTO.getSale());
        yzyDao.updateById(entity);
    }

    private String generateCut(Integer type,Integer sale){
        Integer a = 0;
        Integer b = 0;
        switch (type){
            case 0:
                a = sale/45;
                b = sale/40;
                break;
            case 1:
                a = sale/48;
                b = sale/45;
                break;
            case 2:
                a = sale/70;
                b = sale/60;
                break;
            default:
                a = sale/30;
                b = sale/25;
        }
        return a+"~"+b;
    }
}