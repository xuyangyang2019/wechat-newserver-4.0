package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MsgDelLogQuery extends BaseQuery {

	private Integer cid;
	private String wechatid;
	private String friendid;
	private String issend;
	private Integer contenttype;
	private String content;

}
