package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
@Table(name = "tbl_wx_message")
@Getter
@Setter
public class WxMessageInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;//
	private Integer cid;// 所属客户id
	private String wechatid;
	private String friendid;
	private String msgsvrid;
	private String issend;//true发送消息  false收到的消息
  	private Integer contenttype;//消息内容类型，具体对应proto
	private String content;
	private Integer type;//0聊天消息  1群消息
	@Column(name = "create_time")
	private Date createTime;
	
	private String wechatno;
	private String wechatnick;
	private String friendno;
	private String friendnick;
}
