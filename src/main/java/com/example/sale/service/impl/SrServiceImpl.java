package com.example.sale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sale.model.UserThreadLocal;
import com.example.sale.vo.SrVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.sale.dao.SrDao;
import com.example.sale.entity.SrEntity;
import com.example.sale.service.SrService;

import java.util.ArrayList;
import java.util.List;


@Service("srService")
public class SrServiceImpl extends ServiceImpl<SrDao, SrEntity> implements SrService {

    @Autowired
    private SrDao srDao;


    @Override
    public List<SrVO> getData(Integer target) {
        List<SrEntity> list = srDao.selectList(new LambdaQueryWrapper<SrEntity>().eq(target!=null,SrEntity::getId,target));
        List<SrVO> res = new ArrayList<>();
        for(SrEntity entity:list){
            SrVO srVO = new SrVO();
            BeanUtils.copyProperties(entity,srVO);
            Integer stock = entity.getStock();
            Integer g = entity.getG();
            Integer h = entity.getH();
            Integer inland = stock-g;
            Integer korea = stock-h;
            Integer total = inland+korea;
            srVO.setTotal(total);
            srVO.setD(total/40);
            srVO.setE(total/45);
            res.add(srVO);
        }
        return res;
    }
}