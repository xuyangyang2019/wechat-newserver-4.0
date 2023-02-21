package com.jubotech.business.web.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
@Table(name = "tbl_wx_friendaddtask")
@Getter
@Setter
public class FriendAddTask{
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private Integer accountid;
	private Integer cid;
	private Integer state;// 状态1开启中0已完成
	private String execute_time;// 执行时间
	private Integer between_time;//间隔时间
	private String wechatid;// 微信号
	private String message;// 内容
	private String remarks;// 备注
	private Integer totalsize;
	private Integer doingsize;
	private Integer successsize;
	@Column(name = "create_time")
	private Date createTime;  
	
	@Transient
	private String wechatno;
	@Transient
	private String wechatnick;
}
