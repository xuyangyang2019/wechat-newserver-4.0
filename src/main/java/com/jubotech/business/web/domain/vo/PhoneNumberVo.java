package com.jubotech.business.web.domain.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class PhoneNumberVo implements Serializable {
	private static final long serialVersionUID = 1L;
	  
	private String wechatid;// 微信号
	private String phonenumber;// 电话号码
	private Integer state;
	private Integer task_result;
	private String begin_time;
	private String end_time;
	 
	// pc端定时传参///////////////////
	private Integer pageSize = 20;// 每页多少条
	private Integer pageNo = 1;// 当前第几页
	
}
