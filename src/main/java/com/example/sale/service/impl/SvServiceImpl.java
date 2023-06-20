package com.example.sale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sale.entity.SrEntity;
import com.example.sale.vo.SrVO;
import com.example.sale.vo.SvVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.sale.dao.SvDao;
import com.example.sale.entity.SvEntity;
import com.example.sale.service.SvService;

import java.util.ArrayList;
import java.util.List;


@Service("svService")
public class SvServiceImpl extends ServiceImpl<SvDao, SvEntity> implements SvService {

    @Autowired
    private SvDao svDao;

    @Override
    public List<SvVO> getData(Integer target) {
        List<SvEntity> list = svDao.selectList(new LambdaQueryWrapper<SvEntity>().eq(target!=null,SvEntity::getId,target));
        List<SvVO> res = new ArrayList<>();
        for(SvEntity entity:list){
            SvVO svVO = new SvVO();
            BeanUtils.copyProperties(entity,svVO);
            Integer sale = entity.getSale();
            switch (entity.getType()){
                case 0:
                    sale /= 100;
                    break;
                case 1:
                    sale /= 110;
                    break;
                case 2:
                    sale /= 370;
                    break;
                default:
                    sale /= 400;
            }
            svVO.setNum(sale);
            res.add(svVO);
        }
        return res;
    }
}