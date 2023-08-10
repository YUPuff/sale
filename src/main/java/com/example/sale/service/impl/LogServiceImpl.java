package com.example.sale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sale.dao.UserDao;
import com.example.sale.entity.DataEntity;
import com.example.sale.entity.UserEntity;
import com.example.sale.vo.LogVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.sale.dao.LogDao;
import com.example.sale.entity.LogEntity;
import com.example.sale.service.LogService;

import java.util.ArrayList;
import java.util.List;


@Service("logService")
public class LogServiceImpl extends ServiceImpl<LogDao, LogEntity> implements LogService {

    @Autowired
    private LogDao logDao;

    @Autowired
    private UserDao userDao;

    @Override
    public List<LogVO> getData() {
        List<LogVO> res = new ArrayList<>();
        List<Object> names = userDao.selectObjs(new QueryWrapper<UserEntity>().select("distinct name"));
        for (Object name : names) {
            List<Object> addresses = logDao.selectObjs(new QueryWrapper<LogEntity>().eq("name",name)
                    .select("distinct address"));
            LogVO logVO = new LogVO((String)name,addresses);
            res.add(logVO);
        }
        return res;
    }
}