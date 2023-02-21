package com.jubotech.business.web.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
@Table(name = "tbl_wx_circletask_like")
@Getter
@Setter
public class CircleLike{
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private String circle_wechatid;//朋友圈发布者wechaid
	private String wechatid;
	private String circleid;
	private String friendid;
	private String nickname;
	private String publishtime;
	@Column(name = "create_time")
	private Date createTime;  
}
