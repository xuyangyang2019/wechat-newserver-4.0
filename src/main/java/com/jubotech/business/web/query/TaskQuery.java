package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class TaskQuery extends BaseQuery {
	private Integer cid;
	private Integer accountid;
	private Integer tasktype;// 任务类型1群发消息2发朋友圈
	private Integer state;// 状态1开启中0已完成
	private String remarks;
}
