package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountQuery extends BaseQuery {

	private Integer type;
	private Integer cid;
	private String account;

}
