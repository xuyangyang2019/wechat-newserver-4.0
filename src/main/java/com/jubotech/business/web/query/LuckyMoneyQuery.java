package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LuckyMoneyQuery extends BaseQuery {
	private Integer cid;
	private String wechatid;
	private Integer type;// 0聊天消息 1群消息
	private String start;
	private String end;
}
