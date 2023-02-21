package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class FriendAddTaskQuery extends BaseQuery {
	private Integer accountid;
	private Integer cid;
	private Integer state;// 状态1开启中0已完成
}
