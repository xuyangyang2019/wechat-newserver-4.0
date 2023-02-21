package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "tbl_wx_phonenumber")
@Getter
@Setter
public class PhoneNumberInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private Integer cid;
	private Integer tid;
	private Integer state;//0已使用 1为使用
	private String wechatid;// 微信号
	private String phonenumber;// 内容
	private String task_result;
	@Column(name = "create_time")
	private Date createTime;
}
