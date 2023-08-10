package com.example.sale.dao;

import com.example.sale.entity.LogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yilin
 * @date 2023-08-05 12:44:25
 */
@Mapper
public interface LogDao extends BaseMapper<LogEntity> {
	
}
