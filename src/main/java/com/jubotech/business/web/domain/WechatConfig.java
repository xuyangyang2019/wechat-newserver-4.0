package com.jubotech.business.web.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class WechatConfig implements  Serializable{
	private static final long serialVersionUID = 1L;
	private String wechatid;// 微信号
	private Integer add_count;//添加数量
}
