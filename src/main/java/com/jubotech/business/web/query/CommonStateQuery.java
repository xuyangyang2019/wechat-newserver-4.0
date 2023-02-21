package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonStateQuery extends BaseQuery {
	private Integer cid;
	private Integer type;
	private Integer state;
	private Integer adminId;
}
