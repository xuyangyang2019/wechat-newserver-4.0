package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourcesQuery extends BaseQuery {
	private Integer type;
	private Integer cid;
	private String content;
}
