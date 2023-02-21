package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
@Table(name = "tbl_wx_luckymoney")
@Getter
@Setter
public class WxLuckymoney implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;//
	private Integer cid;// 所属客户id
	private String wechatid;
	private String friendid;
	private String friendname;
	private String msgid;
  	private Integer amount;//金额
	private String content;
	private Integer type;//0红包 1转账
	@Column(name = "create_time")
	private Date createTime;
}
