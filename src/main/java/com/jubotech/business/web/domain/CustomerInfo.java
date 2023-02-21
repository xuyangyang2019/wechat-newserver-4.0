package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "tbl_customerinfo")
@Getter
@Setter
public class CustomerInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;// SupplierId
	private String suppliername;
	private Integer account_num;
	private Integer device_num;
	private String validity;
	private Integer state;//状态  0正常  其他异常
	private String admin;
	private String contact;
	private String phone;
	private String description;
	@Column(name = "create_time")
	private Date createTime;
}
