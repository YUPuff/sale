package com.example.sale.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yilin
 * @date 2023-06-25 15:57:08
 */
@Data
@TableName("t_sk")
@ApiModel(value = "Sk对象")
@NoArgsConstructor
@AllArgsConstructor
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
	 * 当前库存
	 */
	@ApiModelProperty(value = "库存")
	private Integer stock;
	/**
	 * 总库存
	 */
	@ApiModelProperty(value = "总库存")
	private Integer stockTotal;
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

	public SkEntity(Integer id, Integer stock) {
		this.id = id;
		this.stock = stock;
	}
}
