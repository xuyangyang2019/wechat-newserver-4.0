package com.jubotech.business.web.domain.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class AddFriendVo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String wechatId;//商家所属微信id(你用哪个微信去加)
	private String phone;//要添加的手机号码(手机号，微信号，QQ)
	private String message;//发送的验证消息
	private String remark; //备注名; 
}
