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
    public List<YzyVO> getData(Integer target) {
        List<YzyEntity> list = yzyDao.selectList(new LambdaQueryWrapper<YzyEntity>().eq(target!=null,YzyEntity::getId,target));
        List<YzyVO> res = new ArrayList<>();
        for (YzyEntity entity:list){
            YzyVO yzyVO = new YzyVO();
            BeanUtils.copyProperties(entity,yzyVO);
            Integer sale = entity.getSale();
            Integer a = 0;
            Integer b = 0;
            switch (entity.getType()){
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
            yzyVO.setCut(a+"~"+b);
            res.add(yzyVO);
        }
        return res;
    }
}