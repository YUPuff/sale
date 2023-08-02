package com.example.sale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sale.common.BusinessException;
import com.example.sale.constant.ResultConstants;
import com.example.sale.dto.CommonDTO;
import com.example.sale.model.UserThreadLocal;
import com.example.sale.vo.SvVO;
import com.example.sale.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.sale.dao.SvDao;
import com.example.sale.entity.SvEntity;
import com.example.sale.service.SvService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service("svService")
public class SvServiceImpl extends ServiceImpl<SvDao, SvEntity> implements SvService, ResultConstants {

    @Autowired
    private SvDao svDao;

    @Override
    public List<SvVO> getData(Integer target) {
        List<SvEntity> list = svDao.selectList(new LambdaQueryWrapper<SvEntity>().eq(target!=null,SvEntity::getId,target));
        List<SvVO> res = new ArrayList<>();
        for(SvEntity entity:list){
            res.add(generateVO(entity));
        }
        return res;
    }

    @Override
    public List<SvVO> getData1(Integer role) {
        List<Integer> ids = new ArrayList<>();
        List<SvVO> res = new ArrayList<>();
        if (role == 8){
            ids = Arrays.asList(9,10,11);
        }else if(role == 9){
            ids = Arrays.asList(11);
        }
        for (Integer id:ids) {
            SvEntity entity = svDao.selectById(id);
            res.add(generateVO(entity));
        }
        return res;
    }

    @Override
    public void edit(CommonDTO commonDTO) {
        SvEntity entity = svDao.selectById(commonDTO.getId());
        if (entity == null)
            throw new BusinessException(NOT_EXIST);
        entity.setSale(commonDTO.getSale());
        svDao.updateById(entity);
    }

    private SvVO generateVO(SvEntity entity){
        SvVO svVO = new SvVO();
        BeanUtils.copyProperties(entity,svVO);
        Integer sale = entity.getSale();
        if (entity.getId() == 11 && UserThreadLocal.get().getRole() != 0)
            svVO.setSale(0);
        Integer a = sale/entity.getBig();
        Integer b = sale/entity.getSmall();
        svVO.setNum(a+"~"+b);
        return svVO;
    }
}