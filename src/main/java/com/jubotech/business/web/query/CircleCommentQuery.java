package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CircleCommentQuery extends BaseQuery{
	private Integer id;
	private Integer cid;
	private String wechatid;
	private String circleid;
	private String comment; 
	private String startTime;
	private String endTime;
}
