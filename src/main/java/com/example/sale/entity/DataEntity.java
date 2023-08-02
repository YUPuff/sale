package com.example.sale.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yilin
 * @date 2023-08-02 10:46:35
 */
@Data
@TableName("t_data")
@ApiModel(value = "Data对象")
public class DataEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 库存
	 */
	private String stock;

	/**
	 * 添加时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date time;

}
