package com.example.sale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sale.common.BusinessException;
import com.example.sale.dao.UserDao;
import com.example.sale.dto.VxDTO;
import com.example.sale.entity.LogEntity;
import com.example.sale.entity.UserEntity;
import com.example.sale.vo.VxVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.sale.dao.VxDao;
import com.example.sale.entity.VxEntity;
import com.example.sale.service.VxService;

import java.util.ArrayList;
import java.util.List;


@Service("vxService")
public class VxServiceImpl extends ServiceImpl<VxDao, VxEntity> implements VxService {

    @Autowired
    private VxDao vxDao;

    @Autowired
    private UserDao userDao;

    @Override
    public List<VxVO> getData() {
        List<VxVO> res = new ArrayList<>();
        List<Object> names = userDao.selectObjs(new QueryWrapper<UserEntity>().select("distinct name"));
        for (Object name : names) {
            List<Object> vxs = vxDao.selectObjs(new QueryWrapper<VxEntity>().eq("name",name)
                    .select("vx"));
            VxVO vxVO = new VxVO((String)name,vxs);
            res.add(vxVO);
        }
        return res;
    }

    @Override
    public void add(VxDTO vxDTO) {
        String name = vxDTO.getName();
        String vx = vxDTO.getVx();
        VxEntity vxEntity = vxDao.selectOne(new LambdaQueryWrapper<VxEntity>()
                .eq(VxEntity::getName,name)
                .eq(VxEntity::getVx,vx));
        if (vxEntity != null)
            throw new BusinessException("请勿重复添加");
        vxEntity = new VxEntity(name,vx,vxDTO.getAddress());
        vxDao.insert(vxEntity);
    }
}