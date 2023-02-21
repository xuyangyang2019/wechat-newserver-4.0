package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class MessageQuery extends BaseQuery {
	private Integer cid;
	private String wechatid;
	private String friendid;
	private String wechatno;
	private String wechatnick;
	private String friendno;
	private String friendnick;
	private String issend;
	private Integer contenttype;//消息类型，具体对应proto文件
	private Integer type;//0聊天消息  1群消息
	private String start;
	private String end;
}
