package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
@Table(name = "tbl_wx_circletask_details")
@Getter
@Setter
public class CircleTaskDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;
	private Integer tid;
	private String wechatid;
	private String json_content;// 内容
	private String execute_time;
	private Integer state;// 状态1开启中0已完成
	private String msgid;
	private String results;
	@Column(name = "create_time")
	private Date createTime;
}
