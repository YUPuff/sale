package com.example.sale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sale.entity.SvEntity;
import com.example.sale.vo.LsVO;
import com.example.sale.vo.SvVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.sale.dao.LsDao;
import com.example.sale.entity.LsEntity;
import com.example.sale.service.LsService;

import java.util.ArrayList;
import java.util.List;


@Service("lsService")
public class LsServiceImpl extends ServiceImpl<LsDao, LsEntity> implements LsService {

    @Autowired
    private LsDao lsDao;

    @Override
    public List<LsVO> getData(Integer target) {
        List<LsEntity> list = lsDao.selectList(new LambdaQueryWrapper<LsEntity>().eq(target!=null,LsEntity::getId,target));
        List<LsVO> res = new ArrayList<>();
        for(LsEntity entity:list){
            LsVO lsVO = new LsVO();
            BeanUtils.copyProperties(entity,lsVO);
            Integer sale = lsVO.getSale();
            Integer a1 = sale/110;
            Integer a2 = sale/100;
            Integer b1 = sale/330;
            Integer b2 = sale/320;
            lsVO.setA(a1+"~"+a2);
            lsVO.setB(b1+"~"+b2);
            res.add(lsVO);
        }
        return res;
    }
}