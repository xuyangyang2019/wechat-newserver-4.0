package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConvDelLogQuery extends BaseQuery {

	private Integer cid;
	private String wechatid;
	private String convid;
	private String convname; 

}
