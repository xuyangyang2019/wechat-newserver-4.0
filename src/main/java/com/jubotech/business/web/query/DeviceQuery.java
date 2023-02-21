package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceQuery extends BaseQuery {
	private Integer isonline;
	private String wechatid;
	private String deviceid;
	private Integer cid;
	private Integer accountid;
	private Integer groupid;
	private String orderby;
}
