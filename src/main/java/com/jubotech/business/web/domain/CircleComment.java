package com.jubotech.business.web.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
@Table(name = "tbl_wx_circletask_comment")
@Getter
@Setter
public class CircleComment{
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private Integer cid;
	private String circle_wechatid;//朋友圈发布者wechaid
	private String wechatid;
	private String circleid;
	private String comment;
	private String fromwechatid;
	private String fromname;
	private String towechatid;
	private String toname;
	private String publishtime;
	private String  commentid;
	private String  flag;
	private String  replycommentid;
	  
	@Column(name = "create_time")
	private Date createTime;  
	
	@Transient
	private String  remarks;
	@Transient
	private Integer taskid;
}
