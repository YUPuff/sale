package com.example.sale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sale.vo.YzyVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.sale.dao.YzyDao;
import com.example.sale.entity.YzyEntity;
import com.example.sale.service.YzyService;

import java.util.ArrayList;
import java.util.List;


@Service("yzyService")
public class YzyServiceImpl extends ServiceImpl<YzyDao, YzyEntity> implements YzyService {

    @Autowired
    private YzyDao yzyDao;

    @Override
    public List<YzyVO> getData() {
        List<YzyEntity> list = yzyDao.selectList(new LambdaQueryWrapper<YzyEntity>());
        List<YzyVO> res = new ArrayList<>();
        for (YzyEntity entity:list){
            YzyVO yzyVO = new YzyVO();
            BeanUtils.copyProperties(entity,yzyVO);
            Integer sale = entity.getSale();
            yzyVO.setB(sale/40);
            yzyVO.setC(sale/45);
            res.add(yzyVO);
        }
        return res;
    }
}