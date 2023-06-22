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
            Integer type = entity.getType();
            if (type<4){
                if (type%2 == 0){
                    Integer a = sale/100;
                    Integer b = sale/110;
                    svVO.setNum(b+"~"+a);
                }else{
                    Integer a = sale/320;
                    Integer b = sale/330;
                    svVO.setNum(b+"~"+a);
                }
            }else{
                if (type%2 == 0){
                    Integer a = sale/40;
                    Integer b = sale/45;
                    svVO.setNum(b+"~"+a);
                }else{
                    Integer a = sale/370;
                    Integer b = sale/380;
                    svVO.setNum(b+"~"+a);
                }
            }


            res.add(svVO);
        }
        return res;
    }
}