package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonTermQuery extends BaseQuery {
	private Integer cid;
	private Integer tid;
	private String name;
	private Integer ctype;
}
