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
            res.add(generateVO(entity));
        }
        return res;
    }

    /**
     * @param role
     * @return
     */
    @Override
    public List<YzyVO> getData1(Integer role) {
        List<YzyVO> res = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        if(role == 13){
            list = Arrays.asList(17,18);
        }else if(role == 9){
            list = Arrays.asList(40);
        }else if (role == 6){
            list = Arrays.asList(52,53,54,55,56,57);
        }else if (role == 7){
            list = Arrays.asList(52,54,56,58);
        }
        for (Integer id : list) {
            YzyEntity entity = yzyDao.selectById(id);
            res.add(generateVO(entity));
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

    private YzyVO generateVO(YzyEntity entity){
        YzyVO yzyVO = new YzyVO();
        BeanUtils.copyProperties(entity,yzyVO);
        Integer sale = yzyVO.getSale();
        Integer a = sale/entity.getBig();
        Integer b = sale/entity.getSmall();
        yzyVO.setCut(a+"~"+b);
        return yzyVO;
    }
//    private String generateCut(Integer type,Integer sale){
//        Integer a = 0;
//        Integer b = 0;
//        switch (type){
//            case 0:
//                a = sale/45;
//                b = sale/40;
//                break;
//            case 1:
//                a = sale/48;
//                b = sale/45;
//                break;
//            case 2:
//                a = sale/70;
//                b = sale/60;
//                break;
//            default:
//                a = sale/30;
//                b = sale/25;
//        }
//        return a+"~"+b;
//    }
}