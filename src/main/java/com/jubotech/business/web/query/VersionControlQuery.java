package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class VersionControlQuery extends BaseQuery{
	private Integer cid;
	private String version;
	private Integer vernumber;
	private String packagename;
	private Integer flag;
}
