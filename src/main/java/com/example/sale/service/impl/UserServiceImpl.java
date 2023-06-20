package com.example.sale.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sale.common.BusinessException;
import com.example.sale.constant.RedisConstants;
import com.example.sale.dto.UserDTO;
import com.example.sale.utils.JWTUtils;
import com.example.sale.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.sale.dao.UserDao;
import com.example.sale.entity.UserEntity;
import com.example.sale.service.UserService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.apache.shiro.SecurityUtils.getSubject;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public UserVO login(UserDTO userDTO) {
        String name = userDTO.getUsername();
        UserEntity user = userDao.selectOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getName,name));
        if (user == null)
            throw new BusinessException("用户不存在");
        if (!userDTO.getPassword().equals(user.getPassword()))
            throw new BusinessException("密码错误");
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        userVO.setName(name);
        generateToken(userVO);
        return userVO;
    }

    @Override
    public UserVO getUserByToken(String token) {
        String json = redisTemplate.opsForValue().get(RedisConstants.TOKEN+token);
        return JSON.parseObject(json,UserVO.class);
    }

    @Override
    public void logout(String token) {
        getSubject().logout();
        redisTemplate.delete(RedisConstants.TOKEN+token);
    }

    private void generateToken(UserVO userVO){
        String token = JWTUtils.sign(userVO.getId());
        redisTemplate.opsForValue().set(RedisConstants.TOKEN+token, JSON.toJSONString(userVO),7, TimeUnit.DAYS);
        userVO.setToken(token);
    }
}