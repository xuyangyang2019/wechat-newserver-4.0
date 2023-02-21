package com.jubotech.business.web.query;

import com.jubotech.framework.common.BaseQuery;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneNumberQuery extends BaseQuery {
	private Integer cid;
	private Integer state;//0已使用 1为使用
	private String wechatid;// 微信号
	private String phonenumber;// 内容
}
