package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceGroupQuery extends BaseQuery {
	private Integer cid;
	private String name;
}