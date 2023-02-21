package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class SMSMessageQuery extends BaseQuery {
	private Integer cid;
	private String number;
    private Integer type;
	private String content;
	private String readz;
	private String imei;
}
