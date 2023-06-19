package com.example.sale.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author yilin
 * @date 2023-06-19 15:50:56
 */
@Data
@TableName("t_yzy")
@ApiModel(value = "Yzy对象")
public class YzyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;

	/**
	 * 名字
	 */
	private String name;

	/**
	 * 销量
	 */
	private Integer sale;

}
