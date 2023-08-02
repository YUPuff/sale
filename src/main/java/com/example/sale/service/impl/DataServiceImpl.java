package com.example.sale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sale.dto.DataDTO;
import com.example.sale.vo.DataVO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.sale.dao.DataDao;
import com.example.sale.entity.DataEntity;
import com.example.sale.service.DataService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Service("dataService")
public class DataServiceImpl extends ServiceImpl<DataDao, DataEntity> implements DataService {

    @Autowired
    private DataDao dataDao;

    @Override
    public List<DataVO> getData(DataDTO dataDTO) {
        List<String> names = Arrays.asList("李帝努","罗渽民","李楷灿","李马克","黄仁俊","朴志晟","钟辰乐");
        List<DataVO> res = new ArrayList<>();
        String start = dataDTO.getStart();
        String end = dataDTO.getEnd();
        System.out.println(start);
        System.out.println(end);
        for (String name : names) {
            List<Object> data = dataDao.selectObjs(new QueryWrapper<DataEntity>()
                    .eq("name",name)
                    .ge(StringUtils.isNotBlank(start),"time",start)
                    .le(StringUtils.isNotBlank(end),"time",end)
                    .select("stock"));
            System.out.println(data);
            res.add(new DataVO(name,data));
        }
        return res;
    }
}