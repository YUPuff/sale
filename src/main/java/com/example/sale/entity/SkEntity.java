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
 * @date 2023-06-25 15:57:08
 */
@Data
@TableName("t_sk")
@ApiModel(value = "Sk对象")
public class SkEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Integer id;
	/**
	 * 名字
	 */
	@ApiModelProperty(value = "名字")
	private String name;
	/**
	 * 微店库存
	 */
	@ApiModelProperty(value = "微店库存")
	private Integer stockVd;
	/**
	 * KMS库存
	 */
	@ApiModelProperty(value = "KMS库存")
	private Integer stockKms;
	/**
	 * 小除数
	 */
	@ApiModelProperty(value = "小除数")
	private Integer small;
	/**
	 * 大除数
	 */
	@ApiModelProperty(value = "大除数")
	private Integer big;

	/**
	 * 微店总量
	 */
	@ApiModelProperty(value = "微店总量")
	private Integer totalVd;
	/**
	 * KMS总量
	 */
	@ApiModelProperty(value = "KMS总量")
	private Integer totalKms;

}
