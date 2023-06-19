package com.example.sale.dao;

import com.example.sale.entity.UserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yilin
 * @date 2023-06-19 15:50:56
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {
	
}
