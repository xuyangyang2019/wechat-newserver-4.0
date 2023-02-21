package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class FriendAddTaskDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer tid;
	private Integer state;// 状态1开启中0已完成
	private String execute_time;
	private String json_content;// 内容
	private String wechatid;// 微信号
	private String phonenumber;
	private Date create_time;

	// pc端定时传参///////////////////
	private Integer pageSize = 20;// 每页多少条
	private Integer pageNo = 1;// 当前第几页

}
