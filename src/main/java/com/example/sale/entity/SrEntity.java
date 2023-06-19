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
@TableName("t_sr")
public class SrEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 销量
	 */
	private Integer stock;

	/**
	 * 
	 */
	private Integer g;

	/**
	 * 
	 */
	private Integer h;

}
