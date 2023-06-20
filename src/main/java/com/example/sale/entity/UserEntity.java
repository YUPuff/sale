package com.example.sale.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yilin
 * @date 2023-06-19 15:50:56
 */
@Data
@TableName("t_user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;

	/**
	 * 用户名
	 */
	private String name;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 角色
	 */
	private Integer role;

	/**
	 * 目标
	 */
	private Integer target;
}
