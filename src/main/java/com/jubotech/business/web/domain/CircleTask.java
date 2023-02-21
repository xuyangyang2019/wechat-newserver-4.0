package com.jubotech.business.web.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
@Table(name = "tbl_wx_circletask")
@Getter
@Setter
public class CircleTask{
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private Integer accountid;
	private Integer cid;
	private Integer state;// 状态1开启中0已完成
	private String execute_time;// 执行时间
	private Integer restype;
	private String content;
	private Integer likesize;
	private Integer commentsize;
	private Integer totalsize;
	@Transient
	private Integer doingsize;
	private String remarks;// 内容
	private String wechats;//要发朋友圈的微信号
	@Column(name = "create_time")
	private Date createTime;  
	private String deleted;
	 
	@Transient
	private String comment;// 评论
	@Transient
	private Integer attachtype;// 附件类型
	@Transient
	private String attachtcontent;// 附件内容
	@Transient
	private Integer between_time;//间隔时间，单位分钟
}
