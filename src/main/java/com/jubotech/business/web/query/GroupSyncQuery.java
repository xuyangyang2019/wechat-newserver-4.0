package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupSyncQuery extends BaseQuery {
	private Integer cid;
	private Integer state;
	private String wechatid;
	private String targetgroupid;
}
