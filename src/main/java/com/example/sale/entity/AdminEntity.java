package com.example.sale.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author yilin
 * @date 2023-02-08 17:35:28
 */
@Data
@TableName("admin")
@ApiModel(value = "Admin对象")
public class AdminEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户Id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 用户名
	 */
	private String name;


	/**
	 * F
	 */
	private Integer f;


	/**
	 * G
	 */
	private Integer g;

	/**
	 * H
	 */
	private Integer h;


}
