package com.jubotech.business.web.domain;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendAddTaskSetting implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer cid;
	private String message;
	private String remarks;
	private Integer accountid;
	private String execute_time;//执行时间
	private Integer between_time;//间隔时间
	private Integer sayhellosize;//打招呼次数
	//private List<String> phones;//电话号码集合
	private List<WechatConfig> wechatConfig;//每个微信号加的数量
}
