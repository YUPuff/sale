package com.example.sale.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * @author yilin
 * @date 2023-08-05 12:44:25
 */
@Data
@TableName("t_log")
@ApiModel(value = "Log对象")
@AllArgsConstructor
public class LogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 
	 */
	private String name;
	/**
	 * 
	 */
	private String ip;

	/**
	 * 
	 */
	private String address;

	/**
	 * 添加时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date time;

	public LogEntity(String name, String ip, String address) {
		this.name = name;
		this.ip = ip;
		this.address = address;
	}
}
