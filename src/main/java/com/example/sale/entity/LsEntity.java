package com.example.sale.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author yilin
 * @date 2023-06-22 10:33:49
 */
@Data
@TableName("t_ls")
@ApiModel(value = "Ls对象")
public class LsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId
	private Integer id;
	/**
	 * 名字
	 */
	private String name;

	private Integer sale;

	private Integer small;

	private Integer big;

}
