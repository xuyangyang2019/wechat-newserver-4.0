package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendChangeQuery extends BaseQuery {
	private Integer cid;
	private Integer accountid;
	private String wechatid;
	private Integer type;// 功能  1增加好友  2删除好友 
	private String start;
	private String end;
}
