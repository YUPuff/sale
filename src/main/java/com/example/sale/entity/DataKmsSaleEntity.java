package com.example.sale.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yilin
 * @date 2023-11-23 10:46:35
 */
@Data
@TableName("t_data_kms_sale")
@ApiModel(value = "DataKmsSale对象")
public class DataKmsSaleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 姓名
	 */
	private String name;

	private String monthSale;

	private String soldNum;

	/**
	 * 添加时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date time;

}
