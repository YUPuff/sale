package com.example.sale.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yilin
 * @date 2023-08-10 00:14:15
 */
@Data
@TableName("t_vx")
@ApiModel(value = "Vx对象")
public class VxEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 账号
	 */
	private String name;
	/**
	 *  对应微信号
	 */
	private String vx;


	private String address;

	public VxEntity(String name, String vx, String address) {
		this.name = name;
		this.vx = vx;
		this.address = address;
	}
}
