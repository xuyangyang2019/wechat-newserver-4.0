package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerQuery extends BaseQuery {
	private String suppliername;
	private String  admin;
}
