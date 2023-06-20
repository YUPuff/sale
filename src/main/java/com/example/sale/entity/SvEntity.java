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
 * @date 2023-06-20 18:56:19
 */
@Data
@TableName("t_sv")
@ApiModel(value = "Sv对象")
public class SvEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 
	 */
	@ApiModelProperty(value = "")
	private String name;
	/**
	 * 
	 */
	@ApiModelProperty(value = "")
	private Integer sale;
	/**
	 * 
	 */
	@ApiModelProperty(value = "")
	private Integer type;

}
