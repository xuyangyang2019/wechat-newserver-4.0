package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CircleTaskQuery  extends BaseQuery {
	private Integer cid;
	private Integer state;// 状态1开启中0已完成
	private Integer restype;
	private String content; 
	private String startTime;
	private String endTime;
	private String deleted;
}
