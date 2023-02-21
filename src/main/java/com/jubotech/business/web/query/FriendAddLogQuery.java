package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendAddLogQuery extends BaseQuery {
	private Integer cid;
	private Integer type;//1按天   2按时间
	private String wechatid;
	private Integer groupid;
	private String start;
	private String end;
}
